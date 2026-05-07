package com.pocketco.domain.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class AddNotionCodeRequest {

        @NotBlank
        private String content;
    }
}