package com.dochiri.tooltestlab.global.config.time;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.ZoneId;

@ConfigurationProperties(prefix = "time")
public record TimeProperties(
        @DefaultValue("Asia/Seoul") ZoneId timezone
) {
}
