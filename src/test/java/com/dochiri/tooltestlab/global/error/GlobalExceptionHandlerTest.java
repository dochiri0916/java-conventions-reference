package com.dochiri.tooltestlab.global.error;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

            // when

            // then
        mockMvc.perform(get("/test/plain-runtime-exception"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("code() 메서드가 없는 RuntimeException은 500 응답을 반환한다 - 직접 호출")
    void handleRuntimeExceptionWithoutCodeMethodDirect() {
            // given
        var ex = new RuntimeException("일반 런타임 예외");

            // when
        ResponseEntity<ProblemDetail> response = handler.handleRuntime(ex);

            // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("code() 메서드는 있지만 title() 메서드가 없는 enum을 반환하는 예외는 정상 처리된다")
    void handleRuntimeExceptionWithCodeEnumWithoutTitle() throws Exception {
            // given

            // when

            // then
        mockMvc.perform(get("/test/exception-with-code-only"))
                .andExpect(status().isInternalServerError());
    }

    @RestController
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    static class ThrowController {

        @GetMapping("/test/plain-runtime-exception")
        String throwPlainRuntimeException() {
            throw new RuntimeException("일반 런타임 예외");
        }

        @GetMapping("/test/exception-with-code-only")
        String throwExceptionWithCodeOnly() {
            throw new ExceptionWithCodeOnly();
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
