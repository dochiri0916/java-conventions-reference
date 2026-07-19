package com.dochiri.tooltestlab.global.error;

import com.dochiri.tooltestlab.cart.application.exception.CartErrorCode;
import com.dochiri.tooltestlab.catalog.category.application.exception.CategoryErrorCode;
import com.dochiri.tooltestlab.catalog.product.application.exception.ProductApplicationErrorCode;
import com.dochiri.tooltestlab.order.application.exception.OrderApplicationErrorCode;
import com.dochiri.tooltestlab.review.application.exception.ReviewApplicationErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiErrorMessageCatalogTest {

    @Autowired
    private ApiErrorMessageCatalog catalog;

    @Test
    @DisplayName("장바구니 오류는 컨텍스트 전용 한국어 메시지를 반환한다")
    void cartErrorHasContextMessage() {
        // given
        var code = ApiErrorCode.from(CartErrorCode.CART_NOT_FOUND);

        // when
        var message = catalog.findByCode(code);

        // then
        assertThat(message.title()).isEqualTo("장바구니 없음");
        assertThat(message.detail()).isEqualTo("장바구니를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("카테고리 오류는 컨텍스트 전용 한국어 메시지를 반환한다")
    void categoryErrorHasContextMessage() {
        // given
        var code = ApiErrorCode.from(CategoryErrorCode.INVALID_INPUT);

        // when
        var message = catalog.findByCode(code);

        // then
        assertThat(message.title()).isEqualTo("입력값 오류");
        assertThat(message.detail()).isEqualTo("입력값이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("주문 오류는 컨텍스트 전용 한국어 메시지를 반환한다")
    void orderErrorHasContextMessage() {
        // given
        var code = ApiErrorCode.from(OrderApplicationErrorCode.ORDER_NOT_FOUND);

        // when
        var message = catalog.findByCode(code);

        // then
        assertThat(message.title()).isEqualTo("주문 없음");
        assertThat(message.detail()).isEqualTo("주문을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("상품 오류는 컨텍스트 전용 한국어 메시지를 반환한다")
    void productErrorHasContextMessage() {
        // given
        var code = ApiErrorCode.from(ProductApplicationErrorCode.PRODUCT_NOT_FOUND);

        // when
        var message = catalog.findByCode(code);

        // then
        assertThat(message.title()).isEqualTo("상품 없음");
        assertThat(message.detail()).isEqualTo("상품을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("리뷰 오류는 컨텍스트 전용 한국어 메시지를 반환한다")
    void reviewErrorHasContextMessage() {
        // given
        var code = ApiErrorCode.from(ReviewApplicationErrorCode.DUPLICATE_REVIEW);

        // when
        var message = catalog.findByCode(code);

        // then
        assertThat(message.title()).isEqualTo("중복 리뷰");
        assertThat(message.detail()).isEqualTo("이미 해당 상품에 리뷰를 작성했습니다.");
    }
}
