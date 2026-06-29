package com.dochiri.tooltestlab;

import com.dochiri.tooltestlab.global.config.ConcurrencyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationPropertiesScan
@SpringBootApplication
public class ToolTestLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolTestLabApplication.class, args);
    }

}
