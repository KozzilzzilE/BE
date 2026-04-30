package com.pocketco.domain.aiCodeReview.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketco.domain.aiCodeReview.dto.AICodeReviewAIResponseDTO;
import com.pocketco.domain.aiCodeReview.entity.AICodeReviewStatus;
import com.pocketco.domain.problem.repository.TimeLimitRepository;
import com.pocketco.domain.user.entity.History;
import com.pocketco.domain.user.exception.HistoryNotFoundException;
import com.pocketco.domain.user.repository.HistoryRepository;
import com.pocketco.global.util.ai.AIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AICodeReviewWorker {
    private final HistoryRepository historyRepository;
    private final AIService aiService;
    private final TimeLimitRepository timeLimitRepository;
    private final ObjectMapper objectMapper;

    private static final int MAX_TRY_COUNT = 2;

    @Transactional
    public void process(Long historyId) {
        History history = historyRepository.findById(historyId)
                .orElseThrow(HistoryNotFoundException::new);

        double timeLimit = timeLimitRepository.findByProblem_IdAndLanguage_Id(history.getProblem().getId(), history.getLanguage().getId())
                .map(t -> t.getTimeLimitMs() / 1000.0)
                .orElse(1.0);

        String prompt = """
                You are an algorithm code review assistant. A user will provide a coding problem and their solution code.
                
                Your task is to analyze the code and return exactly 3 sections in Korean. Follow the output format strictly.
                
                ---
                
                [INPUT]
                - Problem: %s
                - Language: %s
                - Constraints: %s (time: %.2f)
                - User Code: %s
                
                ---
                
                [OUTPUT FORMAT - return exactly this JSON structure, no extra text]
                {
                  "aiReview": "현재 코드의 아쉬운 점을 2~3가지 간결하게 설명. 시간복잡도, 공간복잡도, 가독성 등 관점에서 분석.",
                  "aiImprovement": "이 문제를 풀기 위해 어떤 알고리즘/자료구조를 왜 써야 하는지 설명. 반드시 '이 문제에서 ~한 부분에 ~을 적용하면 ~이유로 효율적입니다' 형식으로 작성.",
                  "aiCode": "핵심 로직만 담은 리팩터링 코드. 주석은 핵심 포인트 1~2줄만. 언어는 입력과 동일하게."
                }
                
                ---
                
                [RULES]
                - Output must be valid JSON only. No markdown, no explanation outside JSON.
                - Ensure the JSON is syntactically correct and parsable.
                - Each Output field must contain meaningful content. Do not return empty or placeholder values.
                - Do not include any text before or after the JSON.
                - Keep each value concise and practical.
                - Use Korean for all text values except code.
                - In aiReview, write exactly 2~3 numbered points (e.g., "1. ...") and separate each point using '\\n'.
                - Use escaped newline characters '\\n' for line breaks inside all JSON string values.
                - Do not use actual line breaks inside any JSON string value.
                - Escape double quotes in code using \\".
                - In aiCode, preserve line breaks using '\\n'.
                - Do not use actual line breaks inside aiCode. Use '\\n' only.
                - Maintain proper indentation.
                - The code must be a partial snippet, not a complete solution.
                """.formatted(
                history.getProblem().getDescription(),
                history.getLanguage().getName(),
                history.getProblem().getConstraints(),
                timeLimit,
                history.getSourceCode());
        try {
            AICodeReviewAIResponseDTO aiResponse = requestAndParseWithRetry(prompt, historyId);

            history.setAiReview(normalizeText(aiResponse.aiReview()));
            history.setAiImprovement(normalizeText(aiResponse.aiImprovement()));
            history.setAiCode(aiResponse.aiCode());
            history.setAiStatus(AICodeReviewStatus.ACCEPTED);
        } catch (Exception e) {
            log.error("AI 코드 리뷰 처리 실패. historyId={}", historyId, e);
            history.setAiStatus(AICodeReviewStatus.SYSTEM_ERROR);
        }
    }

    private AICodeReviewAIResponseDTO requestAndParseWithRetry(String prompt, Long historyId) {
        Exception lastException = null;

        for (int attempt = 0; attempt < MAX_TRY_COUNT; attempt++) {
            try {
                String result = aiService.requestReview(prompt);
                String text = extractGeminiText(result);
                return parseAIResponse(text);
            } catch (Exception e) {
                lastException = e;
                log.warn("AI 코드 리뷰 요청 실패. historyId={}, attempt={}/{}", historyId, attempt + 1, MAX_TRY_COUNT, e);

                if (attempt < MAX_TRY_COUNT - 1) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException("AI 코드 리뷰 재시도 대기 중 인터럽트 발생", ie);
                    }
                }
            }
        }

        throw new IllegalStateException("AI 코드 리뷰 재시도 모두 실패", lastException);
    }

    private String extractGeminiText(String result) throws Exception {
        if (result == null || result.isBlank()) {
            throw new IllegalArgumentException("AI 응답이 비어있습니다.");
        }

        JsonNode root = objectMapper.readTree(result);

        String text = root.path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text")
                .asText();

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Gemini 응답에서 text를 찾을 수 없습니다.");
        }

        return text;
    }

    private AICodeReviewAIResponseDTO parseAIResponse(String result) throws Exception {
        if (result == null || result.isBlank()) {
            throw new IllegalArgumentException("AI 응답이 비어있습니다.");
        }

        String cleaned = result
                .replace("```json", "")
                .replace("```", "")
                .trim();

        int start = cleaned.indexOf("{");
        int end = cleaned.lastIndexOf("}");

        if (start == -1 || end == -1 || end <= start) {
            throw new IllegalArgumentException("AI 응답에서 JSON을 찾을 수 없습니다.");
        }

        String jsonOnly = cleaned.substring(start, end + 1);
        AICodeReviewAIResponseDTO aiResponse = objectMapper.readValue(jsonOnly, AICodeReviewAIResponseDTO.class);
        if (isBlank(aiResponse.aiReview()) || isBlank(aiResponse.aiImprovement()) || isBlank(aiResponse.aiCode())) {
            throw new IllegalArgumentException("AI 응답 필드가 누락되었습니다.");
        }

        return aiResponse;
    }

    private String normalizeText(String text) {
        if (text == null) {
            return null;
        }

        return text
                .replaceAll("[ \\t]+", " ")     // 스페이스/탭 여러 개 → 공백 1개
                .replaceAll(" *\\n *", "\n")    // 줄바꿈 앞뒤 공백 제거
                .replaceAll("\\n{2,}", "\n")    // 줄바꿈 여러 개 → 줄바꿈 1개
                .trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}