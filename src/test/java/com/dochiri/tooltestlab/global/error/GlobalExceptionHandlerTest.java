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
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    @Test
    @DisplayName("잘못된 JSON 본문은 표준 ProblemDetail 응답을 반환한다")
    void handleMalformedJson() throws Exception {
        // given
        var request = post("/test/body")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"value\":");

        // when
        var result = mockMvc.perform(request).andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getResponse().getContentAsString()).contains(
                "GLOBAL.MALFORMED_REQUEST_BODY", "잘못된 요청 본문", "timestamp", "type");
    }

    @Test
    @DisplayName("숫자 요청 파라미터 형식 오류는 표준 ProblemDetail 응답을 반환한다")
    void handleParameterTypeMismatch() throws Exception {
        // given
        var request = get("/test/number").param("value", "not-a-number");

        // when
        var result = mockMvc.perform(request).andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getResponse().getContentAsString()).contains(
                "GLOBAL.PARAMETER_TYPE_MISMATCH", "요청 값 오류", "timestamp", "type");
    }

    @Test
    @DisplayName("필수 요청 파라미터 누락은 표준 ProblemDetail 응답을 반환한다")
    void handleMissingParameter() throws Exception {
        // given
        var request = get("/test/required");

        // when
        var result = mockMvc.perform(request).andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getResponse().getContentAsString()).contains(
                "GLOBAL.MISSING_PARAMETER", "필수 요청값 누락", "timestamp", "type");
    }

    @Test
    @DisplayName("지원하지 않는 HTTP 메서드는 표준 ProblemDetail 응답을 반환한다")
    void handleUnsupportedMethod() throws Exception {
        // given
        var request = post("/test/number");

        // when
        var result = mockMvc.perform(request).andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());
        assertThat(result.getResponse().getContentAsString()).contains(
                "GLOBAL.METHOD_NOT_ALLOWED", "지원하지 않는 메서드", "timestamp", "type");
    }

    @Test
    @DisplayName("존재하지 않는 경로는 표준 ProblemDetail 응답을 반환한다")
    void handleNotFoundRoute() throws Exception {
        // given
        var request = get("/test/does-not-exist");

        // when
        var result = mockMvc.perform(request).andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getResponse().getContentAsString()).contains(
                "GLOBAL.ROUTE_NOT_FOUND", "요청 경로 없음", "timestamp", "type");
    }

    @RestController
    static class ThrowController {

        @PostMapping("/test/body")
        String body(@RequestBody TestBody body) {
            return body.value();
        }

        @GetMapping("/test/number")
        String number(@RequestParam Integer value) {
            return value.toString();
        }

        @GetMapping("/test/required")
        String required(@RequestParam String value) {
            return value;
        }

        @GetMapping("/test/plain-runtime-exception")
        String throwPlainRuntimeException() {
            throw PlainRuntimeException.create();
        }

        @GetMapping("/test/exception-with-code-only")
        String throwExceptionWithCodeOnly() {
            throw new ExceptionWithCodeOnly();
        }
    }

    record TestBody(String value) {
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
