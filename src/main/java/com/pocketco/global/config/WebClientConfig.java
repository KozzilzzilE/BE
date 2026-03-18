package com.pocketco.global.config;

import com.pocketco.global.config.judge0.Judge0Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient(WebClient.Builder builder, Judge0Properties judge0Properties) {
        return builder
                .baseUrl(judge0Properties.getBaseUrl())
                .build();
    }
}