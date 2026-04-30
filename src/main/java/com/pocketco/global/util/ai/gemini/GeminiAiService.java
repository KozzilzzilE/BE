package com.pocketco.global.util.ai.gemini;

import com.pocketco.global.config.ai.gemini.GeminiProperties;
import com.pocketco.global.util.ai.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiAiService implements AIService {
    private final WebClient geminiWebClient;
    private final GeminiProperties properties;

    @Override
    public String requestReview(String prompt) {
        String uri = "/models/" + properties.getModel() + ":generateContent";

        return geminiWebClient.post().uri(uri)
                .bodyValue(Map.of(
                        "contents", List.of(
                                Map.of(
                                        "parts", List.of(
                                                Map.of("text", prompt)
                                        )
                                )
                        )
                ))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}