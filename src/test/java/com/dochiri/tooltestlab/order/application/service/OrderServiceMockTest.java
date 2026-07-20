package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.cart.domain.model.CartItem;
import com.dochiri.tooltestlab.cart.domain.model.CartItems;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.application.exception.ProductApplicationException;
import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.application.exception.OrderApplicationException;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderCommand;
import com.dochiri.tooltestlab.order.application.port.out.OrderEventPort;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private OrderEventPort orderEventPort;

    private CancelOrderService cancelOrderService;
    private GetOrderService getOrderService;
    private PlaceOrderService placeOrderService;

    @BeforeEach
    void setUp() {
        cancelOrderService = new CancelOrderService(orderRepositoryPort, orderEventPort);
        getOrderService = new GetOrderService(orderRepositoryPort);
        placeOrderService = new PlaceOrderService(
                orderRepositoryPort,
                cartRepositoryPort,
                productRepositoryPort,
                orderEventPort);
    }

    private Order createTestOrder(String memberId) {
        var line = new OrderLine(new ProductId("p1"),
                new ProductName("상품"),
                new Quantity(1), new Money(1000));
        var address = new ShippingAddress(new RecipientName("홍길동"), new Phone("01012345678"),
                new StreetAddress("서울시"), new ZipCode("12345"));
        return Order.create(new MemberId(memberId), OrderLines.from(List.of(line)), address);
    }

    private Cart createCartWithItem(String memberId) {
        var cart = Cart.create(new MemberId(memberId));
        var item = new CartItem(new ProductId("p1"), new ProductName("상품"),
                new Quantity(1), new Money(1000));
        return Cart.restore(cart.id(), cart.memberId(), CartItems.from(List.of(item)));
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
                .isInstanceOf(OrderApplicationException.class);
    }

    @Test
    @DisplayName("cancelOrder - 주문 소유자 아님 예외")
    void cancelOrderNotOwner() {
        // given
        var order = createTestOrder("owner1");
        when(orderRepositoryPort.findByPublicId(order.id().value())).thenReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> cancelOrderService.cancel("otherUser", order.id().value()))
                .isInstanceOf(OrderApplicationException.class);
    }

    @Test
    @DisplayName("getOrder - 주문 없음 예외")
    void retrieveOrderNotFound() {
        // given
        when(orderRepositoryPort.findByPublicId("nonexistent")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> getOrderService.getOrder("m1", "nonexistent"))
                .isInstanceOf(OrderApplicationException.class);
    }

    @Test
    @DisplayName("getOrder - 주문 소유자 아님 예외")
    void retrieveOrderNotOwner() {
        // given
        var order = createTestOrder("owner1");
        when(orderRepositoryPort.findByPublicId(order.id().value())).thenReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> getOrderService.getOrder("otherUser", order.id().value()))
                .isInstanceOf(OrderApplicationException.class);
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
                .isInstanceOf(OrderApplicationException.class);
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
                .isInstanceOf(OrderApplicationException.class);
    }

    @Test
    @DisplayName("placeOrder - 상품을 찾을 수 없으면 ProductNotFoundException")
    void placeOrderProductNotFound() {
        // given
        var cmd = createPlaceOrderCommand();
        var cart = createCartWithItem("m1");
        when(cartRepositoryPort.findByMemberId("m1")).thenReturn(Optional.of(cart));
        when(productRepositoryPort.findById("p1")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> placeOrderService.place(cmd))
                .isInstanceOf(ProductApplicationException.class);
    }

    @Test
    @DisplayName("placeOrder - 재고가 부족하면 InsufficientStockException")
    void placeOrderInsufficientStock() {
        // given
        var cmd = createPlaceOrderCommand();
        var cart = createCartWithItem("m1");
        var lowStockProduct = Product.create(
                "상품",
                "설명",
                1000,
                0
        );
        when(cartRepositoryPort.findByMemberId("m1")).thenReturn(Optional.of(cart));
        when(productRepositoryPort.findById("p1")).thenReturn(Optional.of(lowStockProduct));

        // when & then
        assertThatThrownBy(() -> placeOrderService.place(cmd))
                .isInstanceOf(ProductApplicationException.class);
    }
}
