package com.pocketco.global.config.ai.gemini;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
public class GeminiConfig {
    private final GeminiProperties geminiProperties;

    @Bean
    public WebClient geminiWebClient() {

        HttpClient httpClient = HttpClient.create().responseTimeout(geminiProperties.getTimeout());

        return WebClient.builder()
                .baseUrl(geminiProperties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-goog-api-key", geminiProperties.getApiKey())
                .build();
    }
}