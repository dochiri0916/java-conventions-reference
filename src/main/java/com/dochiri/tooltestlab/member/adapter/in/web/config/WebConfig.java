package com.dochiri.tooltestlab.member.adapter.in.web.config;

import com.dochiri.tooltestlab.member.adapter.in.web.LoginCheckInterceptor;
import com.dochiri.tooltestlab.member.adapter.in.web.MemberSessionArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;
    private final MemberSessionArgumentResolver memberSessionArgumentResolver;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/members/register",
                        "/api/members/login",
                        "/api/reviews/product/**",
                        "/h2-console/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberSessionArgumentResolver);
    }
}
