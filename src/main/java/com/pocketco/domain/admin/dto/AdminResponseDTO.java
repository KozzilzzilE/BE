package com.pocketco.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class AdminResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class AddNotionCodeResponse {
        private Long notionId;
        private String languageName;
        private Long notionCodeId;
    }
}