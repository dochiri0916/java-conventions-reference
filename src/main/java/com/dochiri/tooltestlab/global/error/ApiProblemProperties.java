package com.dochiri.tooltestlab.global.error;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.util.Objects;

@ConfigurationProperties("api.problem")
public record ApiProblemProperties(URI baseUri) {

    public ApiProblemProperties {
        Objects.requireNonNull(baseUri, "baseUri");
    }
}
