package com.dochiri.tooltestlab.product.adapter.in.web.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class ProductDescriptionExceptionHandlerTest {

    private final ProductDescriptionExceptionHandler handler =
            new ProductDescriptionExceptionHandler();

    @Test
    @DisplayName("필수 상품 설명 오류를 Bad Request ProblemDetail로 변환한다")
    void handlesRequiredDescription() {
        // given
        InvalidProductDescriptionException exception =
                InvalidProductDescriptionException.required();

        // when
        ProblemDetail problem = handler.handle(exception);

        // then
        assertProblem(
                problem,
                "/problems/product-description-required",
                "상품 설명 입력 오류",
                "상품 설명은 필수입니다."
        );
    }

    @Test
    @DisplayName("짧은 상품 설명 오류를 Bad Request ProblemDetail로 변환한다")
    void handlesTooShortDescription() {
        // given
        InvalidProductDescriptionException exception =
                InvalidProductDescriptionException.tooShort();

        // when
        ProblemDetail problem = handler.handle(exception);

        // then
        assertProblem(
                problem,
                "/problems/product-description-too-short",
                "상품 설명 입력 오류",
                "상품 설명이 최소 길이보다 짧습니다."
        );
    }

    @Test
    @DisplayName("긴 상품 설명 오류를 Bad Request ProblemDetail로 변환한다")
    void handlesTooLongDescription() {
        // given
        InvalidProductDescriptionException exception =
                InvalidProductDescriptionException.tooLong();

        // when
        ProblemDetail problem = handler.handle(exception);

        // then
        assertProblem(
                problem,
                "/problems/product-description-too-long",
                "상품 설명 입력 오류",
                "상품 설명이 최대 길이를 초과했습니다."
        );
    }

    private void assertProblem(
            final ProblemDetail problem,
            final String type,
            final String title,
            final String detail
    ) {
        assertThat(problem.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problem.getType()).isEqualTo(URI.create(type));
        assertThat(problem.getTitle()).isEqualTo(title);
        assertThat(problem.getDetail()).isEqualTo(detail);
        assertThat(problem.getProperties()).isNullOrEmpty();
    }
}
