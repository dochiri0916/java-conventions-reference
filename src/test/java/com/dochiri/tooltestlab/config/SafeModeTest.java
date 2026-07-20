package com.dochiri.tooltestlab.config;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.model.*;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.model.*;
import com.dochiri.tooltestlab.review.application.exception.DuplicateReviewException;
import com.dochiri.tooltestlab.review.application.exception.ReviewApplicationErrorCode;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.domain.model.Rating;
import com.dochiri.tooltestlab.review.domain.model.Review;
import com.dochiri.tooltestlab.review.domain.model.ReviewContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(properties = "tool-test-lab.concurrency.safe=true")
@Transactional
class SafeModeTest {

    @Autowired
    private CartRepositoryPort cartRepositoryPort;
    @Autowired
    private MemberRepositoryPort memberRepositoryPort;
    @Autowired
    private OrderRepositoryPort orderRepositoryPort;
    @Autowired
    private ProductRepositoryPort productRepositoryPort;
    @Autowired
    private ReviewRepositoryPort reviewRepositoryPort;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("safe 모드에서 멤버 저장 및 조회")
    void safeMemberSave() {
        // given
        var member = Member.create(new Email("safe@test.com"), new EncodedPassword("pw"), new MemberName("안전모드"));

        // when
        var saved = memberRepositoryPort.create(member);
        var found = memberRepositoryPort.findByMemberId(saved.id().value());

        // then
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("safe 모드에서 상품 저장 및 조회")
    void safeProductSave() {
        // given
        var product = Product.create("safe상품", "설명", 1000, 10);

        // when
        var saved = productRepositoryPort.save(product, List.of());
        var found = productRepositoryPort.findById(saved.id().value());

        // then
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("safe 모드에서 상품 업데이트")
    void safeProductUpdate() {
        // given
        var product = Product.create("safe상품2", "설명", 2000, 20);
        var saved = productRepositoryPort.save(product, List.of());

        // when
        var decreased = saved.decreaseStock(5);
        var updated = productRepositoryPort.save(decreased, List.of());

        // then
        assertThat(updated.stock().value()).isEqualTo(15);
    }

    @Test
    @DisplayName("safe 모드에서 장바구니 저장 및 조회")
    void safeCartSave() {
        // given
        var cart = Cart.create(new MemberId("safeMember1"));

        // when
        cartRepositoryPort.save(cart);
        var found = cartRepositoryPort.findByMemberId("safeMember1");

        // then
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("safe 모드에서 주문 저장 및 조회")
    void safeOrderSave() {
        // given
        var line = new OrderLine(
                new ProductId("p1"),
                new ProductName("상품"),
                new Quantity(1),
                new Money(1000)
        );

        var address = new ShippingAddress(new RecipientName("홍길동"), new Phone("01012345678"),
                new StreetAddress("서울시"), new ZipCode("12345"));

        var order = Order.create(
                new MemberId("safeM1"),
                OrderLines.from(List.of(line)),
                address
        );

        // when
        var saved = orderRepositoryPort.create(order);
        var found = orderRepositoryPort.findByPublicId(saved.id().value());

        // then
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("safe 모드에서 리뷰 저장 및 조회")
    void safeReviewSave() {
        // given
        var review = Review.create(new com.dochiri.tooltestlab.member.domain.model.MemberId("safeM1"),
                new ProductId("p1"),
                new Rating(5), new ReviewContent("좋아요"));

        // when
        var saved = reviewRepositoryPort.save(review);
        var found = reviewRepositoryPort.findByPublicId(saved.id().value());

        // then
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("safe 모드에서 회원의 주문 목록을 조회할 수 있다")
    void safeOrderFindByMemberId() {
        // given
        var memberId = new MemberId("safeOrdM1");
        var line = new OrderLine(new ProductId("p1"),
                new ProductName("상품"),
                new Quantity(1),
                new Money(1000)
        );

        var address = new ShippingAddress(
                new RecipientName("홍길동"),
                new Phone("01012345678"),
                new StreetAddress("서울시"),
                new ZipCode("12345")
        );

        var order = Order.create(memberId, OrderLines.from(List.of(line)), address);
        orderRepositoryPort.create(order);

        // when
        var orders = orderRepositoryPort.findByMemberId(memberId.value());

        // then
        assertThat(orders).isNotEmpty();
    }

    @Test
    @DisplayName("safe 모드에서 카테고리를 저장하고 publicId로 조회할 수 있다")
    void safeProductFindCategoryByPublicId() {
        // given
        var category = Category.create(new CategoryName("safe-카테고리"));

        // when
        var saved = productRepositoryPort.saveCategory(category);
        var found = productRepositoryPort.findCategoryByPublicId(saved.id().value());

        // then
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("safe 모드에서 상품의 카테고리 목록을 조회할 수 있다")
    void safeProductFindCategoriesByProductPublicId() {
        // given
        var category = Category.create(new CategoryName("safe-카테고리2"));
        var savedCategory = productRepositoryPort.saveCategory(category);
        var product = Product.create("safe상품3", "설명", 3000, 30);
        var savedProduct = productRepositoryPort.save(product, List.of(savedCategory));

        // when
        var categories = productRepositoryPort.findCategoriesByProductPublicId(savedProduct.id().value());

        // then
        assertThat(categories).isNotEmpty();
    }

    @Test
    @DisplayName("safe 모드에서 여러 publicId로 카테고리를 조회할 수 있다")
    void safeProductFindCategoriesByPublicIds() {
        // given
        var category1 = productRepositoryPort.saveCategory(Category.create(new CategoryName("safe-카테고리3")));
        var category2 = productRepositoryPort.saveCategory(Category.create(new CategoryName("safe-카테고리4")));
        var ids = Set.of(category1.id().value(), category2.id().value());

        // when
        var categories = productRepositoryPort.findCategoriesByPublicIds(ids);

        // then
        assertThat(categories).hasSize(2);
    }

    @Test
    @DisplayName("safe 모드에서 상품을 카테고리와 함께 저장할 수 있다")
    void safeProductSaveWithCategories() {
        // given
        var category1 = productRepositoryPort.saveCategory(Category.create(new CategoryName("safe-카테고리5")));
        var category2 = productRepositoryPort.saveCategory(Category.create(new CategoryName("safe-카테고리6")));

        // when
        var product = Product.create("카테고리상품", "설명", 5000, 50);
        var saved = productRepositoryPort.save(product, List.of(category1, category2));
        var categories = productRepositoryPort.findCategoriesByProductPublicId(saved.id().value());

        // then
        assertThat(categories).hasSize(2);
    }

    @Test
    @DisplayName("safe 모드에서 상품별 리뷰 목록을 조회할 수 있다")
    void safeReviewFindByProductId() {
        // given
        var memberId = new com.dochiri.tooltestlab.member.domain.model.MemberId("safeRevM1");
        var productId = new ProductId("safeRevP1");
        reviewRepositoryPort.save(Review.create(memberId, productId,
                new Rating(4), new ReviewContent("첫 번째 리뷰")));

        // when
        var reviews = reviewRepositoryPort.findByProductId(productId.value());

        // then
        assertThat(reviews).isNotEmpty();
    }

    @Test
    @DisplayName("safe 모드에서 동일한 회원이 같은 상품에 중복 리뷰를 작성하면 예외가 발생한다")
    void safeReviewDuplicateThrowsException() {
        // given
        var memberId = new com.dochiri.tooltestlab.member.domain.model.MemberId("safeRevDup");
        var productId = new ProductId("safeRevDupP1");
        var review = Review.create(memberId, productId,
                new Rating(5), new ReviewContent("리뷰 내용"));
        reviewRepositoryPort.save(review);
        // 중복 검사 쿼리가 flush를 트리거하여 첫 번째 리뷰가 DB에 반영되도록 한다
        reviewRepositoryPort.existsByMemberIdAndProductId(memberId.value(), productId.value());
        var duplicate = Review.create(memberId, productId,
                new Rating(3), new ReviewContent("또 다른 리뷰"));

        // when & then
        assertThatThrownBy(() -> reviewRepositoryPort.save(duplicate))
                .isInstanceOf(DuplicateReviewException.class)
                .extracting(exception -> ((DuplicateReviewException) exception).code())
                .isEqualTo(ReviewApplicationErrorCode.DUPLICATE_REVIEW);
    }

    @Test
    @DisplayName("safe 모드에서 동시 생성으로 인한 UNIQUE 제약 위반 시 재시도 로직이 실행된다")
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    void safeCartRetryOnUniqueConstraintViolation() {
        // given
        var memberId = new MemberId("safeCartDupTest");
        var cart = Cart.create(memberId);
        var cartIdValue = cart.id().value();

        // cartId는 같지만 memberId는 다른 행을 직접 삽입하여 cartId UNIQUE 충돌 유도
        jdbcTemplate.update(
                "INSERT INTO carts (cart_id, member_id, version) VALUES (?, ?, 0)",
                cartIdValue, "differentMemberId");

        // when
        Exception caught = null;
        try {
            cartRepositoryPort.save(cart);
        } catch (Exception e) {
            caught = e;
        }

        // then
        assertThat(caught).isNotNull();
    }

    @Test
    @DisplayName("safe 모드에서 상품 목록을 페이지네이션으로 조회할 수 있다")
    void safeProductFindAll() {
        // given
        var product = Product.create("safe상품페이지", "설명", 1000, 10);
        productRepositoryPort.save(product, List.of());

        // when
        var page = productRepositoryPort.findAll(0, 20);

        // then
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("safe 모드에서 전체 카테고리 목록을 조회할 수 있다")
    void safeProductFindAllCategories() {
        // given
        productRepositoryPort.saveCategory(Category.create(new CategoryName("safe-전체조회")));

        // when
        var categories = productRepositoryPort.findAllCategories();

        // then
        assertThat(categories).isNotEmpty();
    }

    @Test
    @DisplayName("safe 모드에서 상품 업데이트 시 카테고리도 함께 갱신된다")
    void safeProductUpdateWithCategories() {
        // given
        var category = Category.create(new CategoryName("safe-갱신카테고리"));
        var savedCategory = productRepositoryPort.saveCategory(category);
        var product = Product.create("safe갱신상품", "설명", 2000, 20);
        var saved = productRepositoryPort.save(product, List.of());

        // when
        var decreased = saved.decreaseStock(3);
        var updated = productRepositoryPort.save(decreased, List.of(savedCategory));

        // then
        assertThat(updated.stock().value()).isEqualTo(17);
        var categories = productRepositoryPort.findCategoriesByProductPublicId(updated.id().value());
        assertThat(categories).hasSize(1);
    }

    @Test
    @DisplayName("safe 모드에서 리뷰를 publicId로 삭제할 수 있다")
    void safeReviewDeleteByPublicId() {
        // given
        var memberId = new com.dochiri.tooltestlab.member.domain.model.MemberId("safeRevDel");
        var productId = new ProductId("safeRevDelP1");
        var review = Review.create(memberId, productId,
                new Rating(4), new ReviewContent("삭제될 리뷰"));
        var saved = reviewRepositoryPort.save(review);

        // when
        reviewRepositoryPort.deleteByPublicId(saved.id().value());

        // then
        var found = reviewRepositoryPort.findByPublicId(saved.id().value());
        assertThat(found).isEmpty();
    }
}
