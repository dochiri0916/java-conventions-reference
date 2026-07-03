package com.dochiri.tooltestlab.global.error;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalErrorConfig {

    @Bean
    public ApiProblemDetailFactory apiProblemDetailFactory(final ApiProblemProperties properties) {
        return new ApiProblemDetailFactory(properties.baseUri());
    }
}
