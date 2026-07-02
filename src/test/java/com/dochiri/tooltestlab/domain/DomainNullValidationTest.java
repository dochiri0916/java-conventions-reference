package com.dochiri.tooltestlab.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.dochiri.tooltestlab.cart.domain.model.CartId;
import com.dochiri.tooltestlab.cart.domain.model.CartItems;
import com.dochiri.tooltestlab.category.domain.model.CategoryId;
import com.dochiri.tooltestlab.category.domain.model.CategoryName;
import com.dochiri.tooltestlab.order.domain.model.OrderId;
import com.dochiri.tooltestlab.order.domain.model.Phone;
import com.dochiri.tooltestlab.order.domain.model.RecipientName;
import com.dochiri.tooltestlab.order.domain.model.StreetAddress;
import com.dochiri.tooltestlab.order.domain.model.ZipCode;
import com.dochiri.tooltestlab.product.domain.model.ProductDescription;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.review.domain.model.ReviewContent;
import com.dochiri.tooltestlab.review.domain.model.ReviewId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressFBWarnings(value = {"NP", "RV"}, justification = "의도적인 null 입력 검증 테스트")
class DomainNullValidationTest {

    @Test
    @DisplayName("CategoryName/CategoryId null/blank 예외")
    void categoryVoValidation() {
        // given
        String nullInput = null;
        var blankInput = "  ";
        // when & then
        assertThatThrownBy(() -> new CategoryName(nullInput))
                .isInstanceOf(com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryNameException.class);
        assertThatThrownBy(() -> new CategoryName(blankInput))
                .isInstanceOf(com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryNameException.class);
        assertThatThrownBy(() -> new CategoryId(nullInput))
                .isInstanceOf(com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryIdException.class);
        assertThatThrownBy(() -> new CategoryId(blankInput))
                .isInstanceOf(com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryIdException.class);
    }

    @Test
    @DisplayName("ProductName/ProductId/ProductDescription null 예외")
    void productVoNullValidation() {
        // given
        String nullInput = null;
        // when & then
        assertThatThrownBy(() -> new ProductName(nullInput))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductNameException.class);
        assertThatThrownBy(() -> new ProductId(nullInput))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException.class);
        assertThatThrownBy(() -> new ProductDescription(nullInput))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductException.class);
    }

    @Test
    @DisplayName("ReviewContent/ReviewId null 예외")
    void reviewVoNullValidation() {
        // given
        String nullInput = null;
        // when & then
        assertThatThrownBy(() -> new ReviewContent(nullInput))
                .isInstanceOf(com.dochiri.tooltestlab.review.domain.exception.InvalidReviewContentException.class);
        assertThatThrownBy(() -> new ReviewId(nullInput))
                .isInstanceOf(com.dochiri.tooltestlab.review.domain.exception.InvalidReviewIdException.class);
    }

    @Test
    @DisplayName("Order shipping VO null 예외")
    void orderShippingVoNullValidation() {
        // given
        String nullInput = null;
        // when & then
        assertThatThrownBy(() -> new OrderId(nullInput))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException.class);
        assertThatThrownBy(() -> new StreetAddress(nullInput))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException.class);
        assertThatThrownBy(() -> new ZipCode(nullInput))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException.class);
        assertThatThrownBy(() -> new Phone(nullInput))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException.class);
        assertThatThrownBy(() -> new RecipientName(nullInput))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException.class);
    }

    @Test
    @DisplayName("CartId null 예외 / CartItems 메서드 null 예외")
    void cartNullValidation() {
        // given
        String nullInput = null;
        var items = CartItems.empty();
        var nullProductId = (ProductId) null;
        // when & then
        assertThatThrownBy(() -> new CartId(nullInput))
                .isInstanceOf(com.dochiri.tooltestlab.cart.domain.exception.InvalidCartIdException.class);
        assertThatThrownBy(() -> items.containsProduct(nullProductId))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException.class);
        assertThatThrownBy(() -> items.findByProductId(nullProductId))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException.class);
        assertThatThrownBy(() -> items.remove(nullProductId))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException.class);
    }
}
