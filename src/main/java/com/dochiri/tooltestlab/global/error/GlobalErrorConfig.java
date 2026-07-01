package com.dochiri.tooltestlab.global.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class GlobalErrorConfig {

    @Bean
    public ApiProblemDetailFactory apiProblemDetailFactory(@Value("${api.problem.base-uri}") final URI baseUri) {
        return new ApiProblemDetailFactory(baseUri);
    }
}
