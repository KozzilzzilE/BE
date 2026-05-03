package com.pocketco.domain.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

public class AdminRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class AddNotionCodeRequest {
        private Long topicId;
        private Integer pageNo;
        private String title;
        private String point;
        private String detail;
        private List<CodeContentRequest> codes;
    }

    @Getter
    @NoArgsConstructor
    public static class CodeContentRequest {
        private Long languageId;
        private String content;
    }
}