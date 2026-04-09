package com.pocketco.global.config.judge0;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "judge0")
@Getter
@Setter
public class Judge0Properties {
    private String baseUrl;
    private String authnToken;
}