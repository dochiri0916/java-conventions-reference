package com.dochiri.tooltestlab.global.config.time;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class TimeConfig {

    @Bean
    Clock clock(final TimeProperties properties) {
        return Clock.system(properties.timezone());
    }

}
