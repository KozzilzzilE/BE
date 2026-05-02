package com.pocketco.domain.user.dto;

import lombok.*;

public class UserResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageResponse {
        private String email;
        private String nickname;
        private String language;
        private Long solvedProblemCount;
    }
}
