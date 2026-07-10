package com.dochiri.tooltestlab.global.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class ApiTraceIdFilterTest {

    @Test
    @DisplayName("요청 trace ID를 응답과 MDC에 전달하고 처리 후 MDC를 정리한다")
    void usesRequestedTraceId() throws ServletException, IOException {
        // given
        final ApiTraceIdFilter filter = new ApiTraceIdFilter();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(ApiTraceIdFilter.TRACE_ID_HEADER, " trace-123 ");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicReference<String> traceIdDuringChain = new AtomicReference<>();
        final FilterChain chain = (servletRequest, servletResponse) ->
                traceIdDuringChain.set(MDC.get(ApiTraceIdFilter.TRACE_ID_MDC_KEY));

        // when
        filter.doFilter(request, response, chain);

        // then
        assertThat(response.getHeader(ApiTraceIdFilter.TRACE_ID_HEADER)).isEqualTo("trace-123");
        assertThat(traceIdDuringChain).hasValue("trace-123");
        assertThat(MDC.get(ApiTraceIdFilter.TRACE_ID_MDC_KEY)).isNull();
    }

    @Test
    @DisplayName("요청 trace ID가 없으면 새 trace ID를 응답에 설정한다")
    void generatesTraceIdWhenRequestHeaderIsMissing() throws ServletException, IOException {
        // given
        final ApiTraceIdFilter filter = new ApiTraceIdFilter();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicReference<String> traceIdDuringChain = new AtomicReference<>();
        final FilterChain chain = (servletRequest, servletResponse) ->
                traceIdDuringChain.set(MDC.get(ApiTraceIdFilter.TRACE_ID_MDC_KEY));

        // when
        filter.doFilter(request, response, chain);

        // then
        final String responseTraceId = response.getHeader(ApiTraceIdFilter.TRACE_ID_HEADER);
        assertThat(responseTraceId).isNotBlank().hasSize(32).doesNotContain("-");
        assertThat(traceIdDuringChain).hasValue(responseTraceId);
        assertThat(MDC.get(ApiTraceIdFilter.TRACE_ID_MDC_KEY)).isNull();
    }
}
