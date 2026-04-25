package com.pocketco.global.util.judge0;

import com.pocketco.domain.judge0.dto.Judge0IndividualRequest;
import com.pocketco.domain.judge0.dto.Judge0RunResultResponse;
import com.pocketco.domain.judge0.dto.Judge0StatusResponse;
import com.pocketco.domain.judge0.dto.Judge0TokenResponse;
import com.pocketco.global.config.judge0.Judge0Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class Judge0Client {

    private final WebClient webClient;
    private final Judge0Properties judge0Properties;

    public Judge0TokenResponse submit(Judge0IndividualRequest request) {
        return webClient.post()
                .uri("/submissions?wait=false")
                .header("X-Auth-Token", judge0Properties.getAuthnToken())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Judge0TokenResponse.class)
                .block();
    }

    public Judge0StatusResponse getStatus(String token) {
        if (token == null ||  token.isBlank()) {
            throw new IllegalArgumentException("token is null or empty");
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/submissions/{token}")
                        .queryParam("fields", "status,time,wall_time")
                        .build(token))
                .header("X-Auth-Token", judge0Properties.getAuthnToken())
                .retrieve()
                .bodyToMono(Judge0StatusResponse.class)
                .block();
    }

    public Judge0RunResultResponse getRunResult(String token) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/submissions/{token}")
                        .queryParam("fields", "stdin,stdout,status,time")
                        .build(token))
                .header("X-Auth-Token", judge0Properties.getAuthnToken())
                .retrieve()
                .bodyToMono(Judge0RunResultResponse.class)
                .block();
    }

    public String getLanguagesJson() {
        return webClient.get()
                .uri("/languages")
                .header("X-Auth-Token", judge0Properties.getAuthnToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}