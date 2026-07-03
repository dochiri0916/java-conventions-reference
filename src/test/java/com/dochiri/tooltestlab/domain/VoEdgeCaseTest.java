package com.dochiri.tooltestlab.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.dochiri.tooltestlab.cart.domain.model.CartId;
import com.dochiri.tooltestlab.category.domain.model.CategoryId;
import com.dochiri.tooltestlab.category.domain.model.CategoryName;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.OrderId;
import com.dochiri.tooltestlab.order.domain.model.Phone;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.order.domain.model.RecipientName;
import com.dochiri.tooltestlab.order.domain.model.StreetAddress;
import com.dochiri.tooltestlab.order.domain.model.ZipCode;
import com.dochiri.tooltestlab.product.domain.model.ProductDescription;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.product.domain.model.Stock;
import com.dochiri.tooltestlab.review.domain.model.Rating;
import com.dochiri.tooltestlab.review.domain.model.ReviewContent;
import com.dochiri.tooltestlab.review.domain.model.ReviewId;
import com.dochiri.tooltestlab.product.domain.exception.InvalidMaxCountException;
import com.dochiri.tooltestlab.product.domain.exception.InvalidMoneyException;
import com.dochiri.tooltestlab.product.domain.exception.ProductDomainErrorCode;
import com.dochiri.tooltestlab.product.domain.model.MaxCount;
import com.dochiri.tooltestlab.product.domain.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressFBWarnings(value = "RV", justification = "Return value used to verify immutability or exception")
class VoEdgeCaseTest {

    // ── MemberId ──
    @Test
    @DisplayName("MemberId blank 예외")
    void memberIdBlank() {
        // given
        var blankValue = "  ";

        // when & then
        assertThatThrownBy(() -> new MemberId(blankValue))
                .isInstanceOf(RuntimeException.class);
    }

    // ── ProductId ──
    @Test
    @DisplayName("ProductId 정상 생성 및 generate")
    void productId() {
        // given
        var idValue = "prod1";

        // when
        var id = new ProductId(idValue);
        var gen = ProductId.generate();

        // then
        assertThat(id.value()).isEqualTo(idValue);
        assertThat(gen.value()).hasSize(32);
    }

    @Test
    @DisplayName("ProductId blank 예외")
    void productIdBlank() {
        // given
        var blankValue = "  ";

        // when & then
        assertThatThrownBy(() -> new ProductId(blankValue))
                .isInstanceOf(RuntimeException.class);
    }

    // ── ProductName ──
    @Test
    @DisplayName("ProductName blank 예외")
    void productNameBlank() {
        // given
        var blankValue = "  ";

        // when & then
        assertThatThrownBy(() -> new ProductName(blankValue))
                .isInstanceOf(RuntimeException.class);
    }

    // ── ProductDescription ──
    @Test
    @DisplayName("ProductDescription blank 예외")
    void productDescriptionBlank() {
        // given
        var blankValue = "  ";

        // when & then
        assertThatThrownBy(() -> new ProductDescription(blankValue))
                .isInstanceOf(RuntimeException.class);
    }

    // ── Stock ──
    @Test
    @DisplayName("Stock 음수 예외")
    void stockNegative() {
        // given
        var negativeValue = -1;

        // when & then
        assertThatThrownBy(() -> new Stock(negativeValue))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Stock 감소 예외")
    void stockDecreaseNegative() {
        // given
        var stock = new Stock(10);
        var zeroAmount = 0;
        var excessAmount = 11;

        // when & then
        assertThatThrownBy(() -> stock.decrease(zeroAmount))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> stock.decrease(excessAmount))
                .isInstanceOf(RuntimeException.class);
    }

    // ── Quantity ──
    @Test
    @DisplayName("Quantity 0 이하 예외")
    void quantityZeroOrLess() {
        // given
        var zeroValue = 0;
        var negativeValue = -1;

        // when & then
        assertThatThrownBy(() -> new Quantity(zeroValue))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new Quantity(negativeValue))
                .isInstanceOf(RuntimeException.class);
    }

    // ── OrderId ──
    @Test
    @DisplayName("OrderId blank 예외 및 generate")
    void orderId() {
        // given
        var blankValue = "  ";

        // when
        var gen = OrderId.generate();

        // then
        assertThatThrownBy(() -> new OrderId(blankValue))
                .isInstanceOf(RuntimeException.class);
        assertThat(gen.value()).hasSize(32);
    }

    // ── CartId ──
    @Test
    @DisplayName("CartId blank 예외 및 generate")
    void cartId() {
        // given
        var blankValue = "  ";

        // when
        var gen = CartId.generate();

        // then
        assertThatThrownBy(() -> new CartId(blankValue))
                .isInstanceOf(RuntimeException.class);
        assertThat(gen.value()).hasSize(32);
    }

    // ── CategoryId ──
    @Test
    @DisplayName("CategoryId blank 예외 및 generate")
    void categoryId() {
        // given
        var blankValue = "  ";

        // when
        var gen = CategoryId.generate();

        // then
        assertThatThrownBy(() -> new CategoryId(blankValue))
                .isInstanceOf(RuntimeException.class);
        assertThat(gen.value()).hasSize(32);
    }

    // ── CategoryName ──
    @Test
    @DisplayName("CategoryName blank 예외")
    void categoryNameBlank() {
        // given
        var blankValue = "  ";

        // when & then
        assertThatThrownBy(() -> new CategoryName(blankValue))
                .isInstanceOf(RuntimeException.class);
    }

    // ── Rating ──
    @Test
    @DisplayName("Rating 경계값")
    void ratingBoundary() {
        // given
        var tooSmallValue = 0;
        var tooLargeValue = 6;
        var minValid = 1;
        var maxValid = 5;

        // when
        var validMin = new Rating(minValid);
        var validMax = new Rating(maxValid);

        // then
        assertThatThrownBy(() -> new Rating(tooSmallValue))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new Rating(tooLargeValue))
                .isInstanceOf(RuntimeException.class);
        assertThat(validMin.value()).isEqualTo(minValid);
        assertThat(validMax.value()).isEqualTo(maxValid);
    }

    // ── ReviewContent ──
    @Test
    @DisplayName("ReviewContent blank 예외")
    void reviewContentBlank() {
        // given
        var blankValue = "  ";

        // when & then
        assertThatThrownBy(() -> new ReviewContent(blankValue))
                .isInstanceOf(RuntimeException.class);
    }

    // ── ReviewId ──
    @Test
    @DisplayName("ReviewId blank 예외 및 generate")
    void reviewId() {
        // given
        var blankValue = "  ";

        // when
        var gen = ReviewId.generate();

        // then
        assertThatThrownBy(() -> new ReviewId(blankValue))
                .isInstanceOf(RuntimeException.class);
        assertThat(gen.value()).hasSize(32);
    }

    // ── Shipping VOs ──
    @Test
    @DisplayName("Shipping VOs blank 예외")
    void shippingVosBlank() {
        // given
        var blankValue = "  ";

        // when & then
        assertThatThrownBy(() -> new RecipientName(blankValue))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new Phone(blankValue))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new StreetAddress(blankValue))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new ZipCode(blankValue))
                .isInstanceOf(RuntimeException.class);
    }

    // ── MaxCount ──
    @Test
    @DisplayName("MaxCount 0 이하 예외")
    void maxCountInvalid() {
        // given
        var zeroValue = 0;
        var negativeValue = -1;

        // when & then
        assertThatThrownBy(() -> MaxCount.of(zeroValue))
                .isInstanceOf(InvalidMaxCountException.class)
                .extracting(e -> ((InvalidMaxCountException) e).code())
                .isEqualTo(ProductDomainErrorCode.MAX_COUNT_NOT_POSITIVE);
        assertThatThrownBy(() -> MaxCount.of(negativeValue))
                .isInstanceOf(InvalidMaxCountException.class);
    }

    // ── Money ──
    @Test
    @DisplayName("Money 연산")
    void moneyOperations() {
        // given
        var hundred = Money.of(100);
        var twoHundred = Money.of(200);
        var expectedSum = Money.of(300);
        var expectedMultiplied = Money.of(300);
        var negativeValue = -1;

        // when
        var sum = hundred.add(twoHundred);
        var multiplied = hundred.multiply(3);

        // then
        assertThat(Money.ZERO.amount()).isEqualTo(0);
        assertThat(sum).isEqualTo(expectedSum);
        assertThat(multiplied).isEqualTo(expectedMultiplied);
        assertThatThrownBy(() -> Money.of(negativeValue))
                .isInstanceOf(InvalidMoneyException.class)
                .extracting(e -> ((InvalidMoneyException) e).code())
                .isEqualTo(ProductDomainErrorCode.MONEY_NEGATIVE);
    }

    @Test
    @DisplayName("Money add에 null을 전달하면 InvalidMoneyException이 발생한다")
    void moneyAddNull() {
        // given
        var zero = Money.ZERO;

        // when & then
        assertThatThrownBy(() -> zero.add(null))
                .isInstanceOf(InvalidMoneyException.class)
                .extracting(e -> ((InvalidMoneyException) e).code())
                .isEqualTo(ProductDomainErrorCode.MONEY_ADD_NULL);
    }

    @Test
    @DisplayName("Money multiply에 음수를 전달하면 InvalidMoneyException이 발생한다")
    void moneyMultiplyNegative() {
        // given
        var money = Money.of(100);
        var negativeMultiplier = -1;

        // when & then
        assertThatThrownBy(() -> money.multiply(negativeMultiplier))
                .isInstanceOf(InvalidMoneyException.class)
                .extracting(e -> ((InvalidMoneyException) e).code())
                .isEqualTo(ProductDomainErrorCode.MONEY_MULTIPLY_NEGATIVE);
    }

    @Test
    @DisplayName("Quantity.from 정상")
    void quantityFrom() {
        // given
        var value = 5;

        // when
        var q = Quantity.from(value);

        // then
        assertThat(q.value()).isEqualTo(value);
    }

    @Test
    @DisplayName("Stock.increase 정상")
    void stockIncrease() {
        // given
        var stock = new Stock(5);
        var increaseAmount = 3;

        // when
        var increased = stock.increase(increaseAmount);

        // then
        assertThat(increased.value()).isEqualTo(8);
    }

    @Test
    @DisplayName("Stock - 0 확인 및 양수 확인")
    void stockMethods() {
        // given
        var zeroStock = new Stock(0);
        var positiveStock = new Stock(1);

        // when
        var zeroIsZero = zeroStock.isZero();
        var positiveIsPositive = positiveStock.isPositive();
        var zeroIsPositive = zeroStock.isPositive();

        // then
        assertThat(zeroIsZero).isTrue();
        assertThat(positiveIsPositive).isTrue();
        assertThat(zeroIsPositive).isFalse();
    }

    @Test
    @DisplayName("OrderLine 총 가격")
    void orderLineTotalPrice2() {
        // given
        var productId = new com.dochiri.tooltestlab.product.domain.model.ProductId("p1");
        var productName = new com.dochiri.tooltestlab.product.domain.model.ProductName("상품");
        var quantity = new Quantity(3);
        var unitPrice = Money.of(1000);

        // when
        var line = new com.dochiri.tooltestlab.order.domain.model.OrderLine(
                productId, productName, quantity, unitPrice);

        // then
        assertThat(line.totalPrice()).isEqualTo(Money.of(3000));
    }

    @Test
    @DisplayName("OrderLines 팩토리 생성")
    void orderLinesFromFactory() {
        // given
        var productId = new com.dochiri.tooltestlab.product.domain.model.ProductId("p1");
        var productName = new com.dochiri.tooltestlab.product.domain.model.ProductName("상품");
        var quantity = new Quantity(1);
        var unitPrice = Money.of(1000);
        var line = new com.dochiri.tooltestlab.order.domain.model.OrderLine(
                productId, productName, quantity, unitPrice);

        // when
        var lines = com.dochiri.tooltestlab.order.domain.model.OrderLines.from(java.util.List.of(line));

        // then
        assertThat(lines.size()).isEqualTo(1);
    }
}
