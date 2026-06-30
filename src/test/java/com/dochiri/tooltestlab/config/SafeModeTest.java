package com.dochiri.tooltestlab.config;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.model.*;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.model.*;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.model.Product;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.domain.model.Rating;
import com.dochiri.tooltestlab.review.domain.model.Review;
import com.dochiri.tooltestlab.review.domain.model.ReviewContent;
import com.dochiri.tooltestlab.shared.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "tool-test-lab.concurrency.safe=true")
@Transactional
class SafeModeTest {

    @Autowired private CartRepositoryPort cartRepositoryPort;
    @Autowired private MemberRepositoryPort memberRepositoryPort;
    @Autowired private OrderRepositoryPort orderRepositoryPort;
    @Autowired private ProductRepositoryPort productRepositoryPort;
    @Autowired private ReviewRepositoryPort reviewRepositoryPort;

    @Test
    @DisplayName("safe 모드에서 멤버 저장 및 조회")
    void safeMemberSave() {
            // given

            // when

            // then

        var member = Member.create(new Email("safe@test.com"), new Password("pw"), new MemberName("안전모드"));
        var saved = memberRepositoryPort.save(member);
        var found = memberRepositoryPort.findByPublicId(saved.id().value());
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("safe 모드에서 상품 저장 및 조회")
    void safeProductSave() {
            // given

            // when

            // then

        var product = Product.create("safe상품", "설명", Money.of(1000), 10);
        var saved = productRepositoryPort.save(product, List.of());
        var found = productRepositoryPort.findByPublicId(saved.id().value());
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("safe 모드에서 상품 업데이트")
    void safeProductUpdate() {
            // given

            // when

            // then

        var product = Product.create("safe상품2", "설명", Money.of(2000), 20);
        var saved = productRepositoryPort.save(product, List.of());
        var decreased = saved.decreaseStock(5);
        var updated = productRepositoryPort.save(decreased, List.of());
        assertThat(updated.stock().value()).isEqualTo(15);
    }

    @Test
    @DisplayName("safe 모드에서 장바구니 저장 및 조회")
    void safeCartSave() {
            // given

            // when

            // then

        var cart = Cart.create(new MemberId("safeMember1"));
        var saved = cartRepositoryPort.save(cart);
        var found = cartRepositoryPort.findByMemberId("safeMember1");
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("safe 모드에서 주문 저장 및 조회")
    void safeOrderSave() {
            // given

            // when

            // then

        var line = new OrderLine(new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                new com.dochiri.tooltestlab.product.domain.model.ProductName("상품"),
                new Quantity(1), Money.of(1000));
        var address = new ShippingAddress(new RecipientName("홍길동"), new Phone("01012345678"),
                new StreetAddress("서울시"), new ZipCode("12345"));
        var order = Order.create(new com.dochiri.tooltestlab.member.domain.model.MemberId("safeM1"),
                OrderLines.from(List.of(line)), address);
        var saved = orderRepositoryPort.save(order);
        var found = orderRepositoryPort.findByPublicId(saved.id().value());
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("safe 모드에서 리뷰 저장 및 조회")
    void safeReviewSave() {
            // given

            // when

            // then

        var review = Review.create(new com.dochiri.tooltestlab.member.domain.model.MemberId("safeM1"),
                new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                new Rating(5), new ReviewContent("좋아요"));
        var saved = reviewRepositoryPort.save(review);
        var found = reviewRepositoryPort.findByPublicId(saved.id().value());
        assertThat(found).isPresent();
    }
}
