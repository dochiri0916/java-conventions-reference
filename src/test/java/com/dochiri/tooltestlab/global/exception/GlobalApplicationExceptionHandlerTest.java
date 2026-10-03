package com.dochiri.tooltestlab.global.exception;

import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalApplicationExceptionHandlerTest {

    private final ProblemDetails problemDetails = new ProblemDetails(
            Clock.fixed(Instant.parse("2026-10-04T00:00:00Z"), ZoneOffset.UTC));
    private final GlobalApiControllerAdvice fallback = spy(new GlobalApiControllerAdvice(problemDetails));
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
            .setControllerAdvice(fallback, new GlobalApplicationExceptionHandler(problemDetails))
            .build();

    @Test
    @DisplayName("저장소 작업 예외는 전용 핸들러가 처리하고 내부 오류 정보를 노출하지 않는다")
    void handlesRepositoryOperationException() throws Exception {
        // given
        final String path = "/test/repository";

        // when
        final var result = mockMvc.perform(get(path));

        // then
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.detail").value("일시적인 오류가 발생했습니다."))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.timestamp").value("2026-10-04T00:00:00Z"));
        verify(fallback, never()).handleUnhandledException(any(Exception.class), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("다른 미처리 예외는 기존 전역 핸들러가 처리한다")
    void retainsFallbackHandling() throws Exception {
        // given
        final String path = "/test/unhandled";

        // when
        final var result = mockMvc.perform(get(path));

        // then
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.detail").value("일시적인 오류가 발생했습니다."));
        verify(fallback).handleUnhandledException(any(IllegalStateException.class), any(HttpServletRequest.class));
    }

    @RestController
    static class TestController {

        @GetMapping("/test/repository")
        public void repository() {
            throw new RepositoryOperationException(new IllegalStateException("internal storage failure"));
        }

        @GetMapping("/test/unhandled")
        public void unhandled() {
            throw new IllegalStateException("unexpected failure");
        }
    }
}
