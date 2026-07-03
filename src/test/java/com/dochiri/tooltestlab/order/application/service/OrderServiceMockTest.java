package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.cart.domain.model.CartItem;
import com.dochiri.tooltestlab.cart.domain.model.CartItems;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.application.exception.EmptyCartException;
import com.dochiri.tooltestlab.order.application.exception.NotOrderOwnerException;
import com.dochiri.tooltestlab.order.application.exception.OrderNotFoundException;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderCommand;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.model.Order;
import com.dochiri.tooltestlab.order.domain.model.OrderLine;
import com.dochiri.tooltestlab.order.domain.model.OrderLines;
import com.dochiri.tooltestlab.order.domain.model.Phone;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.order.domain.model.RecipientName;
import com.dochiri.tooltestlab.order.domain.model.ShippingAddress;
import com.dochiri.tooltestlab.order.domain.model.StreetAddress;
import com.dochiri.tooltestlab.order.domain.model.ZipCode;
import com.dochiri.tooltestlab.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.exception.InsufficientStockException;
import com.dochiri.tooltestlab.product.domain.model.Money;
import com.dochiri.tooltestlab.product.domain.model.Product;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.product.domain.model.ProductStatus;
import com.dochiri.tooltestlab.product.domain.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceMockTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private CartRepositoryPort cartRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private CancelOrderService cancelOrderService;
    private GetOrderService getOrderService;
    private PlaceOrderService placeOrderService;

    @BeforeEach
    void setUp() {
        cancelOrderService = new CancelOrderService(orderRepositoryPort, eventPublisher);
        getOrderService = new GetOrderService(orderRepositoryPort);
        placeOrderService = new PlaceOrderService(orderRepositoryPort, cartRepositoryPort, productRepositoryPort, eventPublisher);
    }

    private Order createTestOrder(String memberId) {
        var line = new OrderLine(new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                new com.dochiri.tooltestlab.product.domain.model.ProductName("상품"),
                new Quantity(1), Money.of(1000));
        var address = new ShippingAddress(new RecipientName("홍길동"), new Phone("01012345678"),
                new StreetAddress("서울시"), new ZipCode("12345"));
        return Order.create(new MemberId(memberId), OrderLines.from(List.of(line)), address);
    }

    private Cart createCartWithItem(String memberId) {
        var cart = Cart.create(new MemberId(memberId));
        var item = new CartItem(new ProductId("p1"), new ProductName("상품"),
                new Quantity(1), Money.of(1000));
        return new Cart(cart.id(), cart.memberId(), CartItems.from(List.of(item)));
    }

    private PlaceOrderCommand createPlaceOrderCommand() {
        var address = new ShippingAddress(new RecipientName("홍길동"), new Phone("01012345678"),
                new StreetAddress("서울시"), new ZipCode("12345"));
        return new PlaceOrderCommand(new MemberId("m1"), address);
    }

    @Test
    @DisplayName("cancelOrder - 주문 없음 예외")
    void cancelOrderNotFound() {
        // given
        when(orderRepositoryPort.findByPublicId("nonexistent")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cancelOrderService.cancel("m1", "nonexistent"))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("cancelOrder - 주문 소유자 아님 예외")
    void cancelOrderNotOwner() {
        // given
        var order = createTestOrder("owner1");
        when(orderRepositoryPort.findByPublicId(order.id().value())).thenReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> cancelOrderService.cancel("otherUser", order.id().value()))
                .isInstanceOf(NotOrderOwnerException.class);
    }

    @Test
    @DisplayName("getOrder - 주문 없음 예외")
    void retrieveOrderNotFound() {
        // given
        when(orderRepositoryPort.findByPublicId("nonexistent")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> getOrderService.getOrder("m1", "nonexistent"))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("getOrder - 주문 소유자 아님 예외")
    void retrieveOrderNotOwner() {
        // given
        var order = createTestOrder("owner1");
        when(orderRepositoryPort.findByPublicId(order.id().value())).thenReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> getOrderService.getOrder("otherUser", order.id().value()))
                .isInstanceOf(NotOrderOwnerException.class);
    }

    // ── PlaceOrderService ──

    @Test
    @DisplayName("placeOrder - 장바구니를 찾을 수 없으면 EmptyCartException")
    void placeOrderCartNotFound() {
        // given
        var cmd = createPlaceOrderCommand();
        when(cartRepositoryPort.findByMemberId("m1")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> placeOrderService.place(cmd))
                .isInstanceOf(EmptyCartException.class);
    }

    @Test
    @DisplayName("placeOrder - 장바구니가 비어있으면 EmptyCartException")
    void placeOrderEmptyCart() {
        // given
        var cmd = createPlaceOrderCommand();
        var emptyCart = Cart.create(new MemberId("m1"));
        when(cartRepositoryPort.findByMemberId("m1")).thenReturn(Optional.of(emptyCart));

        // when & then
        assertThatThrownBy(() -> placeOrderService.place(cmd))
                .isInstanceOf(EmptyCartException.class);
    }

    @Test
    @DisplayName("placeOrder - 상품을 찾을 수 없으면 ProductNotFoundException")
    void placeOrderProductNotFound() {
        // given
        var cmd = createPlaceOrderCommand();
        var cart = createCartWithItem("m1");
        when(cartRepositoryPort.findByMemberId("m1")).thenReturn(Optional.of(cart));
        when(productRepositoryPort.findByPublicId("p1")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> placeOrderService.place(cmd))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("placeOrder - 재고가 부족하면 InsufficientStockException")
    void placeOrderInsufficientStock() {
        // given
        var cmd = createPlaceOrderCommand();
        var cart = createCartWithItem("m1");
        var lowStockProduct = new Product(new ProductId("p1"), new ProductName("상품"),
                new com.dochiri.tooltestlab.product.domain.model.ProductDescription("설명"),
                Money.of(1000), new Stock(0), ProductStatus.SOLD_OUT);
        when(cartRepositoryPort.findByMemberId("m1")).thenReturn(Optional.of(cart));
        when(productRepositoryPort.findByPublicId("p1")).thenReturn(Optional.of(lowStockProduct));

        // when & then
        assertThatThrownBy(() -> placeOrderService.place(cmd))
                .isInstanceOf(InsufficientStockException.class);
    }
}
