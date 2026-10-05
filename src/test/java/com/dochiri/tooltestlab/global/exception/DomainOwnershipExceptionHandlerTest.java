package com.dochiri.tooltestlab.global.exception;

import com.dochiri.tooltestlab.order.adapter.in.web.OrderExceptionHandler;
import com.dochiri.tooltestlab.order.domain.exception.NotOrderOwnerException;
import com.dochiri.tooltestlab.review.adapter.in.web.ReviewExceptionHandler;
import com.dochiri.tooltestlab.review.domain.exception.NotReviewOwnerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.ProblemDetail;

import java.time.Clock;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DomainOwnershipExceptionHandlerTest {

    static Stream<Arguments> ownershipFailures() {
        final ProblemDetails problemDetails = new ProblemDetails(Clock.systemUTC());
        final OrderExceptionHandler order = new OrderExceptionHandler(problemDetails);
        final ReviewExceptionHandler review = new ReviewExceptionHandler(problemDetails);
        return Stream.of(
                Arguments.of((Supplier<ProblemDetail>) () -> order.handle(new NotOrderOwnerException()), "NOT_ORDER_OWNER"),
                Arguments.of((Supplier<ProblemDetail>) () -> review.handle(new NotReviewOwnerException()), "NOT_REVIEW_OWNER"));
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("ownershipFailures")
    @DisplayName("소유자 검증이 도메인으로 이동해도 403과 기존 오류 코드를 유지한다")
    void preservesForbiddenResponses(final Supplier<ProblemDetail> handler, final String expectedCode) {
        final ProblemDetail problem = handler.get();

        assertEquals(403, problem.getStatus());
        assertNotNull(problem.getProperties());
        assertEquals(expectedCode, problem.getProperties().get("code"));
    }
}
