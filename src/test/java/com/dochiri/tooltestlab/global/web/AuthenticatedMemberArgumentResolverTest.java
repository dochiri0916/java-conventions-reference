package com.dochiri.tooltestlab.global.web;

import com.dochiri.tooltestlab.global.error.AuthenticationRequiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticatedMemberArgumentResolverTest {

    private AuthenticatedMemberArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new AuthenticatedMemberArgumentResolver();
    }

    @Test
    @DisplayName("@AuthenticatedMember String 파라미터를 지원한다")
    void supportsAuthenticatedMemberAnnotation() throws NoSuchMethodException {
        // given
        var method = StubController.class.getDeclaredMethod("authenticated", String.class);
        var parameter = MethodParameter.forExecutable(method, 0);

        // when
        var result = resolver.supportsParameter(parameter);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("@AuthenticatedMember가 없는 파라미터는 지원하지 않는다")
    void doesNotSupportParameterWithoutAnnotation() throws NoSuchMethodException {
        // given
        var method = StubController.class.getDeclaredMethod("notAuthenticated", String.class);
        var parameter = MethodParameter.forExecutable(method, 0);

        // when
        var result = resolver.supportsParameter(parameter);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("타입이 String이 아닌 @AuthenticatedMember 파라미터는 지원하지 않는다")
    void doesNotSupportNonStringType() throws NoSuchMethodException {
        // given
        var method = StubController.class.getDeclaredMethod("authenticatedWrongType", Integer.class);
        var parameter = MethodParameter.forExecutable(method, 0);

        // when
        var result = resolver.supportsParameter(parameter);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("HttpServletRequest가 null이면 인증 필요 예외를 던진다")
    void nullRequestThrowsAuthenticationRequired() throws NoSuchMethodException {
        // given
        var method = StubController.class.getDeclaredMethod("authenticated", String.class);
        var parameter = MethodParameter.forExecutable(method, 0);
        var webRequest = mock(NativeWebRequest.class);
        when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> resolver.resolveArgument(parameter, null, webRequest, null))
                .isInstanceOf(AuthenticationRequiredException.class);
    }

    @Test
    @DisplayName("세션이 null이면 인증 필요 예외를 던진다")
    void nullSessionThrowsAuthenticationRequired() throws NoSuchMethodException {
        // given
        var method = StubController.class.getDeclaredMethod("authenticated", String.class);
        var parameter = MethodParameter.forExecutable(method, 0);
        var servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getSession(false)).thenReturn(null);
        var webRequest = new ServletWebRequest(servletRequest);

        // when & then
        assertThatThrownBy(() -> resolver.resolveArgument(parameter, null, webRequest, null))
                .isInstanceOf(AuthenticationRequiredException.class);
    }

    @Test
    @DisplayName("세션에 MEMBER_ID가 null이면 인증 필요 예외를 던진다")
    void nullMemberIdThrowsAuthenticationRequired() throws NoSuchMethodException {
        // given
        var method = StubController.class.getDeclaredMethod("authenticated", String.class);
        var parameter = MethodParameter.forExecutable(method, 0);
        var servletRequest = mock(HttpServletRequest.class);
        var session = mock(HttpSession.class);
        when(servletRequest.getSession(false)).thenReturn(session);
        when(session.getAttribute(ApiAuthenticationInterceptor.MEMBER_ID_KEY)).thenReturn(null);
        var webRequest = new ServletWebRequest(servletRequest);

        // when & then
        assertThatThrownBy(() -> resolver.resolveArgument(parameter, null, webRequest, null))
                .isInstanceOf(AuthenticationRequiredException.class);
    }

    @Test
    @DisplayName("세션에서 MEMBER_ID를 찾아 반환한다")
    void returnsMemberIdFromSession() throws NoSuchMethodException {
        // given
        var method = StubController.class.getDeclaredMethod("authenticated", String.class);
        var parameter = MethodParameter.forExecutable(method, 0);
        var servletRequest = mock(HttpServletRequest.class);
        var session = mock(HttpSession.class);
        when(servletRequest.getSession(false)).thenReturn(session);
        when(session.getAttribute(ApiAuthenticationInterceptor.MEMBER_ID_KEY)).thenReturn("member123");
        var webRequest = new ServletWebRequest(servletRequest);

        // when
        var result = resolver.resolveArgument(parameter, null, webRequest, null);

        // then
        assertThat(result).isEqualTo("member123");
    }

    @SuppressWarnings("unused")
    private static class StubController {
        void authenticated(String memberId) {
        }

        void notAuthenticated(String memberId) {
        }

        void authenticatedWrongType(Integer memberId) {
        }
    }
}
