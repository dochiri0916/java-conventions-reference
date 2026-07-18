package com.dochiri.tooltestlab.product.adapter.in.web.request;

import com.dochiri.tooltestlab.catalog.product.adapter.in.web.request.RegisterProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterProductRequestTest {

    @Test
    @DisplayName("categoryIds가 null이면 empty list로 변환")
    void nullCategoryIds() {
        // given
        var name = "상품";
        var description = "설명";
        var priceAmount = 1000;
        var stock = 10;

        // when
        var req = new RegisterProductRequest(name, description, priceAmount, stock, null);

        // then
        assertThat(req.categoryIds()).isEmpty();
    }

    @Test
    @DisplayName("categoryIds가 있으면 copyOf")
    void nonNullCategoryIds() {
        // given
        var categoryIds = List.of("cat1");

        // when
        var req = new RegisterProductRequest("상품", "설명", 1000, 10, categoryIds);

        // then
        assertThat(req.categoryIds()).containsExactly("cat1");
    }
}
