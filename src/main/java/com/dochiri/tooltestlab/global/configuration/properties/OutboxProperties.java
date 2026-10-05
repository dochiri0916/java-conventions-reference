package com.dochiri.tooltestlab.global.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "messaging.outbox")
public record OutboxProperties(
        @DefaultValue("10000") long sendTimeoutMs,
        @DefaultValue Cleanup cleanup
) {
    public OutboxProperties {
        if (sendTimeoutMs <= 0) {
            throw new IllegalArgumentException("Outbox 전송 제한 시간은 양수여야 합니다.");
        }
    }

    public record Cleanup(
            @DefaultValue("7") long retentionDays,
            @DefaultValue("1000") int batchSize,
            @DefaultValue("10") int maxBatches
    ) {
        public Cleanup {
            if (retentionDays < 0 || batchSize <= 0 || maxBatches <= 0) {
                throw new IllegalArgumentException("Outbox 보관 기간은 0일 이상, 정리 배치 크기와 횟수는 양수여야 합니다.");
            }
        }
    }
}
