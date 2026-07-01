package com.dochiri.tooltestlab.order.adapter.out.persistence;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tool-test-lab.concurrency")
public record ConcurrencyProperties(boolean safe) {
}
