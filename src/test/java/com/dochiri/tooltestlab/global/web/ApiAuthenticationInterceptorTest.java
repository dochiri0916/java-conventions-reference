package com.dochiri.tooltestlab.global.web;

import com.dochiri.tooltestlab.global.error.AuthenticationRequiredException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.method.HandlerMethod;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiAuthenticationInterceptorTest {

    private ApiAuthenticationInterceptor interceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        interceptor = new ApiAuthenticationInterceptor();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("@PublicApi 메서드는 인증 없이 통과한다")
    @SuppressFBWarnings("THROWS")
    void publicApiMethodPassesWithoutAuth() throws Exception {
        // given
        var handler = mock(HandlerMethod.class);
        when(handler.hasMethodAnnotation(PublicApi.class)).thenReturn(true);

        // when
        var result = interceptor.preHandle(request, response, handler);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("@PublicApi 컨트롤러는 인증 없이 통과한다")
    @SuppressFBWarnings("THROWS")
    void publicApiControllerPassesWithoutAuth() throws Exception {
        // given
        var handler = mock(HandlerMethod.class);
        when(handler.hasMethodAnnotation(PublicApi.class)).thenReturn(false);
        when(handler.getBeanType()).thenReturn((Class) PublicApiController.class);

        // when
        var result = interceptor.preHandle(request, response, handler);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("HandlerMethod가 아니면 인증이 필요하다고 판단한다")
    void nonHandlerMethodRequiresAuth() {
        // given
        var handler = new Object();

        // when & then
        assertThatThrownBy(() -> interceptor.preHandle(request, response, handler))
                .isInstanceOf(AuthenticationRequiredException.class);
    }

    @Test
    @DisplayName("세션이 없으면 인증 필요 예외를 던진다")
    void noSessionThrowsAuthenticationRequired() {
        // given
        var handler = mock(HandlerMethod.class);
        when(handler.hasMethodAnnotation(PublicApi.class)).thenReturn(false);
        when(handler.getBeanType()).thenReturn((Class) PrivateController.class);
        when(request.getSession(false)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> interceptor.preHandle(request, response, handler))
                .isInstanceOf(AuthenticationRequiredException.class);
    }

    @Test
    @DisplayName("세션에 MEMBER_ID가 없으면 인증 필요 예외를 던진다")
    void noMemberIdInSessionThrowsAuthenticationRequired() {
        // given
        var handler = mock(HandlerMethod.class);
        when(handler.hasMethodAnnotation(PublicApi.class)).thenReturn(false);
        when(handler.getBeanType()).thenReturn((Class) PrivateController.class);
        var session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(ApiAuthenticationInterceptor.MEMBER_ID_KEY)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> interceptor.preHandle(request, response, handler))
                .isInstanceOf(AuthenticationRequiredException.class);
    }

    @Test
    @DisplayName("세션에 MEMBER_ID가 있으면 통과한다")
    @SuppressFBWarnings("THROWS")
    void memberIdInSessionPasses() throws Exception {
        // given
        var handler = mock(HandlerMethod.class);
        when(handler.hasMethodAnnotation(PublicApi.class)).thenReturn(false);
        when(handler.getBeanType()).thenReturn((Class) PrivateController.class);
        var session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(ApiAuthenticationInterceptor.MEMBER_ID_KEY)).thenReturn("member1");

        // when
        var result = interceptor.preHandle(request, response, handler);

        // then
        assertThat(result).isTrue();
    }

    private static class PublicApiController {
    }

    private static class PrivateController {
    }
}
