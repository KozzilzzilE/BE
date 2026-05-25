package com.pocketco.domain.user.dto;

import lombok.*;
import java.util.List;

public class UserResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageResponse {
        private String profileImgUrl;
        private String email;
        private String nickname;
        private String language;
        private Long solvedProblemCount;
    }
    @Builder
    public record ProfileImageResponse(
            Long profileId,
            String imgUrl
    ) {}

    @Builder
    public record ProfileImageListResponse(
            List<ProfileImageResponse> images
    ) {}
}
