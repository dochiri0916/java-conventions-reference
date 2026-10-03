package com.dochiri.tooltestlab.global.configuration;

import com.dochiri.tooltestlab.global.configuration.properties.TimeProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class TimeConfiguration {

    @Bean
    Clock clock(TimeProperties timeProperties) {
        return Clock.system(ZoneId.of(timeProperties.zone()));
    }

}
