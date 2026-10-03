package com.dochiri.tooltestlab.global.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "time")
public record TimeProperties(
        String zone
) {
    private static final String DEFAULT_TIME_ZONE = "Asia/Seoul";

    public TimeProperties {
        if (zone == null || zone.isBlank()) {
            zone = DEFAULT_TIME_ZONE;
        }
    }
}
