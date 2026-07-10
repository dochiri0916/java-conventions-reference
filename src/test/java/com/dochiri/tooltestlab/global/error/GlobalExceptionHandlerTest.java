package com.dochiri.tooltestlab.global.error;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.exception.MemberErrorCode;

import jakarta.validation.constraints.Min;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

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
    @DisplayName("MDC trace ID가 있으면 ProblemDetail에 같은 값을 기록한다")
    void handleRuntimeExceptionWithTraceId() {
        // given
        final String traceId = "trace-123";
        MDC.put("traceId", traceId);

        try {
            // when
            final ResponseEntity<ProblemDetail> response = handler.handleRuntime(new RuntimeException("오류"));

            // then
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getProperties())
                    .containsEntry(ApiProblemDetailFactory.TRACE_ID_PROPERTY, traceId);
        } finally {
            MDC.remove("traceId");
        }
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

    @Test
    @DisplayName("찾지 못한 핸들러 예외는 경로 없음 오류로 변환한다")
    void handleNoHandlerFoundException() {
        // given
        final NoHandlerFoundException exception = new NoHandlerFoundException(
                "GET", "/test/missing-handler", new HttpHeaders());
        final ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        // when
        final ResponseEntity<Object> response = handler.handleNoHandlerFoundException(
                exception,
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isInstanceOf(ProblemDetail.class);
        final ProblemDetail body = (ProblemDetail) response.getBody();
        assertThat(body.getProperties()).containsEntry(
                ApiProblemDetailFactory.CODE_PROPERTY,
                ApiErrorCode.from(GlobalErrorCode.ROUTE_NOT_FOUND));
    }

    @Test
    @DisplayName("누락된 경로 변수는 표준 ProblemDetail 응답을 반환한다")
    void handleMissingPathVariable() throws Exception {
        // given
        final var request = get("/test/missing-path");

        // when
        final var result = mockMvc.perform(request).andReturn();

        // then
        assertThat(result.getResponse().getContentAsString()).contains(
                "GLOBAL.MISSING_PARAMETER", "필수 요청값 누락", "timestamp", "type");
    }

    @Test
    @DisplayName("누락된 요청 헤더는 표준 ProblemDetail 응답을 반환한다")
    void handleServletRequestBindingException() throws Exception {
        // given
        final var request = get("/test/header");

        // when
        final var result = mockMvc.perform(request).andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getResponse().getContentAsString()).contains(
                "GLOBAL.MISSING_PARAMETER", "필수 요청값 누락", "timestamp", "type");
    }

    @Test
    @DisplayName("지원하지 않는 요청 본문 형식은 표준 ProblemDetail 응답을 반환한다")
    void handleUnsupportedMediaType() throws Exception {
        // given
        final var request = post("/test/body")
                .contentType(MediaType.TEXT_PLAIN)
                .content("plain text");

        // when
        final var result = mockMvc.perform(request).andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        assertThat(result.getResponse().getContentAsString()).contains(
                "GLOBAL.UNSUPPORTED_MEDIA_TYPE", "지원하지 않는 미디어 타입", "timestamp", "type");
    }

    @Test
    @DisplayName("지원하지 않는 응답 형식은 표준 ProblemDetail 응답을 반환한다")
    void handleNotAcceptable() throws Exception {
        // given
        final var request = get("/test/json-only").accept(MediaType.TEXT_PLAIN);

        // when
        final var result = mockMvc.perform(request).andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(result.getResponse().getContentAsString()).contains(
                "GLOBAL.NOT_ACCEPTABLE", "지원하지 않는 응답 형식", "timestamp", "type");
    }

    @Test
    @DisplayName("메서드 검증 오류는 표준 ProblemDetail 응답을 반환한다")
    void handleHandlerMethodValidationException() throws Exception {
        // given
        final var request = get("/test/validated").param("value", "0");

        // when
        final var result = mockMvc.perform(request).andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getResponse().getContentAsString()).contains(
                "GLOBAL.VALIDATION_ERROR", "요청 검증 실패", "timestamp", "type");
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

        @GetMapping("/test/missing-path")
        String missingPath(@PathVariable("id") String id) {
            return id;
        }

        @GetMapping("/test/header")
        String header(@RequestHeader("X-Test-Header") String value) {
            return value;
        }

        @GetMapping(value = "/test/json-only", produces = MediaType.APPLICATION_JSON_VALUE)
        String jsonOnly() {
            return "json";
        }

        @GetMapping("/test/validated")
        String validated(@RequestParam("value") @Min(1) Integer value) {
            return value.toString();
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
