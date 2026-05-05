package com.pocketco.domain.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExerciseBlankDTO {
    @NotBlank
    private String content;

    // Integer여야 null(오답)을 받을 수 있어! 🕵️✨
    private Integer answer;
}