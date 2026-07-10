package com.dochiri.tooltestlab.product.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductExceptionTest {

    @Test
    @DisplayName("InvalidProductIdException 정상 생성")
    void badProductId() {
        // given
        var expectedCode = ProductDomainErrorCode.BAD_PRODUCT_ID;

        // when
        var ex = InvalidProductIdException.badProductId();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidProductIdException required 정상 생성")
    void productIdRequired() {
        // given
        var expectedCode = ProductDomainErrorCode.PRODUCT_ID_REQUIRED;

        // when
        var ex = InvalidProductIdException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidProductNameException 정상 생성")
    void badProductName() {
        // given
        var expectedCode = ProductDomainErrorCode.BAD_PRODUCT_NAME;

        // when
        var ex = InvalidProductNameException.badProductName();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidProductNameException required 정상 생성")
    void productNameRequired() {
        // given
        var expectedCode = ProductDomainErrorCode.PRODUCT_NAME_REQUIRED;

        // when
        var ex = InvalidProductNameException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidStockException 정상 생성")
    void invalidStock() {
        // given
        var expectedCode = ProductDomainErrorCode.INVALID_STOCK;
        var expectedDetail = "detail";

        // when
        var ex = InvalidStockException.invalidStock(expectedDetail);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.detail()).isEqualTo(expectedDetail);
    }

    @Test
    @DisplayName("InvalidStockException required 정상 생성")
    void stockRequired() {
        // given
        var expectedCode = ProductDomainErrorCode.STOCK_REQUIRED;

        // when
        var ex = InvalidStockException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.detail()).isNull();
    }

    @Test
    @DisplayName("InsufficientStockException 정상 생성")
    void lowStock() {
        // given
        var expectedCode = ProductDomainErrorCode.LOW_STOCK;
        var currentStock = 5;
        var requested = 10;

        // when
        var ex = InsufficientStockException.lowStock(currentStock, requested);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.currentStock()).isEqualTo(currentStock);
        assertThat(ex.requested()).isEqualTo(requested);
    }

    @Test
    @DisplayName("InvalidProductException 정상 생성")
    void invalidProduct() {
        // given
        var expectedCode = ProductDomainErrorCode.INVALID_PRODUCT;

        // when
        var ex = InvalidProductException.invalidProduct();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidProductException required 정상 생성")
    void productRequired() {
        // given
        var expectedCode = ProductDomainErrorCode.PRODUCT_REQUIRED;

        // when
        var ex = InvalidProductException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidMoneyException 정상 생성")
    void moneyAddNull() {
        // given
        var expectedCode = ProductDomainErrorCode.MONEY_ADD_NULL;

        // when
        var ex = InvalidMoneyException.addNull();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("음수 금액 예외는 잘못된 금액을 보존한다")
    void negativeMoney() {
        // given
        final long amount = -1L;

        // when
        final var ex = InvalidMoneyException.negative(amount);

        // then
        assertThat(ex.code()).isEqualTo(ProductDomainErrorCode.MONEY_NEGATIVE);
        assertThat(ex.amount()).isEqualTo(amount);
    }

    @Test
    @DisplayName("음수 곱셈 수량 예외는 잘못된 수량을 보존한다")
    void negativeMultiplyQuantity() {
        // given
        final int quantity = -2;

        // when
        final var ex = InvalidMoneyException.multiplyNegative(quantity);

        // then
        assertThat(ex.code()).isEqualTo(ProductDomainErrorCode.MONEY_MULTIPLY_NEGATIVE);
        assertThat(ex.quantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("최대 개수 예외는 잘못된 최대 개수를 보존한다")
    void invalidMaxCount() {
        // given
        final int value = 0;

        // when
        final var ex = InvalidMaxCountException.notPositive(value);

        // then
        assertThat(ex.code()).isEqualTo(ProductDomainErrorCode.MAX_COUNT_NOT_POSITIVE);
        assertThat(ex.value()).isEqualTo(value);
    }

    @Test
    @DisplayName("InvalidMoneyException required 정상 생성")
    void moneyRequired() {
        // given
        var expectedCode = ProductDomainErrorCode.MONEY_REQUIRED;

        // when
        var ex = InvalidMoneyException.required();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("ProductDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = ProductDomainErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.name()).isNotBlank();
        }
    }
}
