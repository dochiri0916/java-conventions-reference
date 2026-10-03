package com.dochiri.tooltestlab.global.configuration;

import com.dochiri.tooltestlab.global.configuration.properties.JpaAuditingProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class JpaAuditingConfiguration {

    @Bean
    AuditorAware<String> securityContextAuditorAware(JpaAuditingProperties jpaAuditingProperties) {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken))
                .map(Authentication::getName)
                .filter(name -> !name.isBlank())
                .or(() -> Optional.of(jpaAuditingProperties.systemSubject()));
    }

    @Bean
    DateTimeProvider auditingDateTimeProvider(Clock clock) {
        return () -> Optional.of(Instant.now(clock));
    }

}
