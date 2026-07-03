package com.dochiri.tooltestlab.global.web;

import com.dochiri.tooltestlab.global.error.AuthenticationRequiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthenticatedMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedMember.class)
                && parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw AuthenticationRequiredException.authenticationRequired();
        }
        final HttpSession session = request.getSession(false);
        if (session == null) {
            throw AuthenticationRequiredException.authenticationRequired();
        }
        final Object memberId = session.getAttribute(ApiAuthenticationInterceptor.MEMBER_ID_KEY);
        if (memberId == null) {
            throw AuthenticationRequiredException.authenticationRequired();
        }
        return memberId;
    }
}
