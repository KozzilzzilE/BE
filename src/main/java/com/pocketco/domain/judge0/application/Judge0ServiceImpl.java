package com.pocketco.domain.judge0.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketco.domain.judge0.dto.Judge0LanguageExternal;
import com.pocketco.domain.judge0.dto.Judge0LanguageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class Judge0ServiceImpl implements Judge0Service {
    private final WebClient webClient;

    @Override
    public List<Judge0LanguageResponse> getJudge0Languages() {
        // judge0의 언어 목록 조회는 GET
        String responseJson =  webClient.get()
                .uri("/languages")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return parseLanguages(responseJson);
    }

    // 응답 JSON을 우리 DTO에 맞게 파싱
    private List<Judge0LanguageResponse> parseLanguages(String responseJson) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            List<Judge0LanguageExternal> externalList =
                    objectMapper.readValue(responseJson, new TypeReference<List<Judge0LanguageExternal>>() { });

            // 우리 API 응답 형식에 맞는 DTO로 매핑
            List<Judge0LanguageResponse> result = externalList.stream()
                    .map(external -> mappingLanguageExternalToResponse(external))
                    .toList();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }

    // 외부 JSON을 형식을 우리 API 응답 JSON 형식에 맞게 매핑
    private Judge0LanguageResponse mappingLanguageExternalToResponse(Judge0LanguageExternal external) {
        return Judge0LanguageResponse.builder()
                .code(external.id())
                .languageName(external.name())
                .build();
    }
}