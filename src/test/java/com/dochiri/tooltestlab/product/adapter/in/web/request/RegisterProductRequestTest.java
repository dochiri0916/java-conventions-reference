package com.dochiri.tooltestlab.product.adapter.in.web.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterProductRequestTest {

    @Test
    @DisplayName("categoryIds가 null이면 empty list로 변환")
    void nullCategoryIds() {
            // given

            // when

            // then

        var req = new RegisterProductRequest("상품", "설명", 1000, 10, null);
        assertThat(req.categoryIds()).isEmpty();
    }

    @Test
    @DisplayName("categoryIds가 있으면 copyOf")
    void nonNullCategoryIds() {
            // given

            // when

            // then

        var req = new RegisterProductRequest("상품", "설명", 1000, 10, List.of("cat1"));
        assertThat(req.categoryIds()).containsExactly("cat1");
    }
}
