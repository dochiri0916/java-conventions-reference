package com.dochiri.tooltestlab.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tool-test-lab.concurrency")
public record ConcurrencyProperties(boolean safe) {
}
