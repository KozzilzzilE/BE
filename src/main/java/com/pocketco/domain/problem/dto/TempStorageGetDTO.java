package com.pocketco.domain.problem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.Instant;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TempStorageGetDTO {
    private Long userCodeId;
    private String sourceCode;
    private String language;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private Instant updatedAt;
}