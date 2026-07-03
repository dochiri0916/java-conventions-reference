package com.dochiri.tooltestlab.global.web;

import com.dochiri.tooltestlab.global.error.AuthenticationRequiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiAuthenticationInterceptor implements HandlerInterceptor {

    public static final String MEMBER_ID_KEY = "MEMBER_ID";

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        if (isPublicApi(handler)) {
            return true;
        }

        final HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(MEMBER_ID_KEY) == null) {
            throw AuthenticationRequiredException.authenticationRequired();
        }

        return true;
    }

    private boolean isPublicApi(final Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return false;
        }
        return handlerMethod.hasMethodAnnotation(PublicApi.class)
                || handlerMethod.getBeanType().isAnnotationPresent(PublicApi.class);
    }
}
