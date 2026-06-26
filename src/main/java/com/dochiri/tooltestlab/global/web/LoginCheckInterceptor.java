package com.dochiri.tooltestlab.global.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 로그인 여부를 체크하는 인터셉터.
 * 세션에 MEMBER_ID가 없으면 401 반환.
 */
public class LoginCheckInterceptor implements HandlerInterceptor {

    public static final String MEMBER_ID_KEY = "MEMBER_ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(MEMBER_ID_KEY) == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\":\"로그인이 필요합니다.\"}");
            return false;
        }

        return true;
    }
}
