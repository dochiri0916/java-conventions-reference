package com.dochiri.tooltestlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class ToolTestLabApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ToolTestLabApplication.class, args);
    }

}
