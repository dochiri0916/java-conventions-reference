package com.dochiri.tooltestlab.global.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.ProblemDetail;

import java.time.Clock;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReferenceIdExceptionHandlerTest {

    static Stream<Arguments> invalidReferences() {
        final ProblemDetails problemDetails = new ProblemDetails(Clock.systemUTC());
        final var cart = new com.dochiri.tooltestlab.cart.adapter.in.web.CartExceptionHandler(problemDetails);
        final var order = new com.dochiri.tooltestlab.order.adapter.in.web.OrderExceptionHandler(problemDetails);
        final var review = new com.dochiri.tooltestlab.review.adapter.in.web.ReviewExceptionHandler(problemDetails);
        return Stream.of(
                Arguments.of((Supplier<ProblemDetail>) () -> cart.handle(
                        new com.dochiri.tooltestlab.cart.domain.exception.InvalidMemberReferenceIdException()), "MEMBER_ID_INVALID"),
                Arguments.of((Supplier<ProblemDetail>) () -> cart.handle(
                        new com.dochiri.tooltestlab.cart.domain.exception.InvalidProductReferenceIdException()), "PRODUCT_ID_INVALID"),
                Arguments.of((Supplier<ProblemDetail>) () -> order.handle(
                        new com.dochiri.tooltestlab.order.domain.exception.InvalidMemberReferenceIdException()), "MEMBER_ID_INVALID"),
                Arguments.of((Supplier<ProblemDetail>) () -> order.handle(
                        new com.dochiri.tooltestlab.order.domain.exception.InvalidProductReferenceIdException()), "PRODUCT_ID_INVALID"),
                Arguments.of((Supplier<ProblemDetail>) () -> review.handle(
                        new com.dochiri.tooltestlab.review.domain.exception.InvalidMemberReferenceIdException()), "MEMBER_ID_INVALID"),
                Arguments.of((Supplier<ProblemDetail>) () -> review.handle(
                        new com.dochiri.tooltestlab.review.domain.exception.InvalidProductReferenceIdException()), "PRODUCT_ID_INVALID"));
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("invalidReferences")
    @DisplayName("잘못된 회원 및 상품 참조 ID는 도메인별 핸들러에서 400으로 처리한다")
    void maps_invalid_reference_ids(final Supplier<ProblemDetail> handler, final String expectedCode) {
        // given
        final int expectedStatus = 400;

        // when
        final ProblemDetail problem = handler.get();

        // then
        assertEquals(expectedStatus, problem.getStatus());
        assertNotNull(problem.getProperties());
        assertEquals(expectedCode, problem.getProperties().get("code"));
    }
}
