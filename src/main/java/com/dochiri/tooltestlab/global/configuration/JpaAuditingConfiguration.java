package com.dochiri.tooltestlab.global.configuration;

import com.dochiri.tooltestlab.global.configuration.properties.JpaAuditingProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class JpaAuditingConfiguration {

    @Bean
    AuditorAware<String> fallbackAuditorAware(JpaAuditingProperties jpaAuditingProperties) {
        return () -> Optional.of(jpaAuditingProperties.systemSubject());
    }

    @Bean
    DateTimeProvider auditingDateTimeProvider(Clock clock) {
        return () -> Optional.of(Instant.now(clock));
    }

}
