package com.dochiri.tooltestlab.global.error;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.exception.MemberErrorCode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@Import(GlobalExceptionHandlerTest.ThrowController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private GlobalExceptionHandler handler;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("code() 메서드가 없는 RuntimeException은 500 응답을 반환한다 - MockMvc")
    void handleRuntimeExceptionWithoutCodeMethodMockMvc() throws Exception {
        // given
        var request = get("/test/plain-runtime-exception");

        // when
        var result = mockMvc.perform(request).andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("code() 메서드가 없는 RuntimeException은 500 응답을 반환한다 - 직접 호출")
    void handleRuntimeExceptionWithoutCodeMethodDirect() {
        // given
        var ex = new RuntimeException("일반 런타임 예외");

        // when
        ResponseEntity<ProblemDetail> response = handler.handleRuntime(ex);
        ProblemDetail body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(body).isNotNull();
        assertThat(body.getDetail()).isEqualTo("예상하지 못한 오류가 발생했습니다.");
        assertThat(body.getDetail()).doesNotContain("일반 런타임 예외");
    }

    @Test
    @DisplayName("회원 예외는 메시지 카탈로그의 한국어 ProblemDetail로 변환된다")
    void handleMemberExceptionWithMessageCatalog() {
        // given
        var exception = DuplicateEmailException.duplicateEmail("test@test.com");

        // when
        ResponseEntity<ProblemDetail> response = handler.handleRuntime(exception);
        ProblemDetail body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(body).isNotNull();
        assertThat(body.getTitle()).isEqualTo("이메일 중복");
        assertThat(body.getDetail()).isEqualTo("이미 사용 중인 이메일입니다.");
        assertThat(body.getProperties()).containsEntry(
                ApiProblemDetailFactory.CODE_PROPERTY,
                ApiErrorCode.from(MemberErrorCode.DUPLICATE_EMAIL));
    }

    @Test
    @DisplayName("매핑되지 않은 RuntimeException은 500 응답을 반환한다")
    void handleRuntimeExceptionWithCodeEnumWithoutTitle() throws Exception {
        // given
        var request = get("/test/exception-with-code-only");

        // when
        var result = mockMvc.perform(request).andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @RestController
    static class ThrowController {

        @GetMapping("/test/plain-runtime-exception")
        String throwPlainRuntimeException() {
            throw PlainRuntimeException.create();
        }

        @GetMapping("/test/exception-with-code-only")
        String throwExceptionWithCodeOnly() {
            throw new ExceptionWithCodeOnly();
        }
    }

    static class PlainRuntimeException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        static PlainRuntimeException create() {
            return new PlainRuntimeException();
        }

        private PlainRuntimeException() {
            super("일반 런타임 예외");
        }
    }

    static class ExceptionWithCodeOnly extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public CodeOnly code() {
            return CodeOnly.SOME_ERROR;
        }
    }

    enum CodeOnly {
        SOME_ERROR
    }
}
