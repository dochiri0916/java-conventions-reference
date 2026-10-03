package com.dochiri.tooltestlab.global.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jpa.audit")
public record JpaAuditingProperties(
        String systemSubject
) {

    private static final String DEFAULT_SYSTEM_SUBJECT = "system";

    public JpaAuditingProperties {
        if (systemSubject == null || systemSubject.isBlank()) {
            systemSubject = DEFAULT_SYSTEM_SUBJECT;
        }

        systemSubject = systemSubject.strip();
    }

}
