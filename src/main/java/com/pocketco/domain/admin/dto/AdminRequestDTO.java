package com.pocketco.domain.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class AddNotionCodeRequest {
        private String content;
    }
}