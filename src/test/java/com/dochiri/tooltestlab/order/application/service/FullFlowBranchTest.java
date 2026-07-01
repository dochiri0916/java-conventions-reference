package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.cart.application.port.in.AddToCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.AddToCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.GetCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.RemoveFromCartUseCase;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.application.port.in.*;
import com.dochiri.tooltestlab.order.domain.model.*;
import com.dochiri.tooltestlab.review.application.port.in.GetProductReviewsUseCase;
import com.dochiri.tooltestlab.review.application.port.in.WriteReviewCommand;
import com.dochiri.tooltestlab.review.application.port.in.WriteReviewUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FullFlowBranchTest {

    @Autowired private AddToCartUseCase addToCartUseCase;
    @Autowired private GetCartUseCase getCartUseCase;
    @Autowired private RemoveFromCartUseCase removeFromCartUseCase;
    @Autowired private PlaceOrderUseCase placeOrderUseCase;
    @Autowired private GetOrderUseCase getOrderUseCase;
    @Autowired private ListOrdersUseCase listOrdersUseCase;
    @Autowired private CancelOrderUseCase cancelOrderUseCase;
    @Autowired private WriteReviewUseCase writeReviewUseCase;
    @Autowired private GetProductReviewsUseCase getProductReviewsUseCase;

    @Test
    @DisplayName("전체 주문 흐름 - 장바구니 추가부터 주문 취소까지")
    void fullOrderFlow() {
        // given
        String memberId = "member1";
        var shippingAddress = new ShippingAddress(
                new RecipientName("홍길동"), new Phone("01012345678"),
                new StreetAddress("서울시"), new ZipCode("12345"));

        // when
        addToCartUseCase.add(new AddToCartCommand(memberId, "prod01notebook000000000000000", 1));
        var cart = getCartUseCase.getCart(memberId);
        var orderResult = placeOrderUseCase.place(new PlaceOrderCommand(new MemberId(memberId), shippingAddress));
        var orderDetail = getOrderUseCase.getOrder(memberId, orderResult.publicId());
        var orders = listOrdersUseCase.listByMember(memberId);
        cancelOrderUseCase.cancel(memberId, orderResult.publicId());

        // then
        assertThat(cart.items()).isNotEmpty();
        assertThat(orderResult.publicId()).isNotNull();
        assertThat(orderDetail.status()).isNotNull();
        assertThat(orders.orders()).isNotEmpty();
    }

    @Test
    @DisplayName("리뷰 작성 및 조회")
    void reviewFlow() {
        // given
        var command = new WriteReviewCommand("member1", "prod01notebook000000000000000", 5, "좋아요!");
        var productId = "prod01notebook000000000000000";

        // when
        writeReviewUseCase.write(command);
        var reviews = getProductReviewsUseCase.getByProduct(productId);

        // then
        assertThat(reviews.reviews()).isNotEmpty();
    }
}
