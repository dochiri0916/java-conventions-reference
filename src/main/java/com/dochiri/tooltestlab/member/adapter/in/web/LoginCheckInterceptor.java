package com.dochiri.tooltestlab.member.adapter.in.web;

import com.dochiri.tooltestlab.member.application.exception.AuthenticationRequiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    public static final String MEMBER_ID_KEY = "MEMBER_ID";

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        final HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(MEMBER_ID_KEY) == null) {
            throw AuthenticationRequiredException.authenticationRequired();
        }

        return true;
    }
}
