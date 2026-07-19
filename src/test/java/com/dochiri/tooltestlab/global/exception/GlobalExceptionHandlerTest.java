package com.dochiri.tooltestlab.global.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("비즈니스 예외를 ErrorCode 기반 ProblemDetail로 변환한다")
    void handleBusinessException() {
        // given
        final BusinessException exception = new TestBusinessException();

        // when
        final ProblemDetail problemDetail = handler.handleBusinessException(exception);

        // then
        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problemDetail.getDetail()).isEqualTo(TestErrorCode.INVALID_INPUT.detail());
        assertThat(problemDetail.getProperties())
                .containsEntry("code", TestErrorCode.INVALID_INPUT.code())
                .containsEntry("field", "name");
    }

    @Test
    @DisplayName("낙관적 잠금 실패를 충돌 ProblemDetail로 변환한다")
    void handleOptimisticLockingFailure() {
        // given
        final var exception = new ObjectOptimisticLockingFailureException("Product", "product-id");

        // when
        final ProblemDetail problemDetail = handler.handleOptimisticLockingFailure(exception);

        // then
        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(problemDetail.getDetail()).isEqualTo(GlobalErrorCode.CONCURRENT_MODIFICATION.detail());
        assertThat(problemDetail.getProperties())
                .containsEntry("code", GlobalErrorCode.CONCURRENT_MODIFICATION.code());
    }

    @Test
    @DisplayName("미처리 예외의 내부 메시지를 응답에 노출하지 않는다")
    void handleUnhandledException() {
        // given
        final var exception = new IllegalStateException("노출하면 안 되는 내부 메시지");
        final var request = new MockHttpServletRequest("GET", "/products");

        // when
        final ProblemDetail problemDetail = handler.handleUnhandledException(exception, request);

        // then
        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(problemDetail.getDetail()).isEqualTo(GlobalErrorCode.INTERNAL_SERVER_ERROR.detail());
        assertThat(problemDetail.getProperties())
                .containsEntry("code", GlobalErrorCode.INTERNAL_SERVER_ERROR.code());
    }

    private enum TestErrorCode implements ErrorCode {

        INVALID_INPUT("TEST_INVALID_INPUT", ErrorKind.INVALID_INPUT, "입력값이 올바르지 않습니다.");

        private final String code;
        private final ErrorKind kind;
        private final String detail;

        TestErrorCode(final String code, final ErrorKind kind, final String detail) {
            this.code = code;
            this.kind = kind;
            this.detail = detail;
        }

        @Override
        public String code() {
            return code;
        }

        @Override
        public ErrorKind kind() {
            return kind;
        }

        @Override
        public String detail() {
            return detail;
        }

    }

    private static final class TestBusinessException extends DomainException {

        private TestBusinessException() {
            super(
                    TestErrorCode.INVALID_INPUT,
                    "로그에서만 사용할 상세 메시지",
                    Map.of("field", "name")
            );
        }

    }

}
