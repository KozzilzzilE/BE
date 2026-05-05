package com.pocketco.domain.admin.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class AddExerciseAppliedCodeRequest {
    @NotBlank
    private String codeTemplate;

    @NotEmpty
    @Valid // 👈 중요: 리스트 안의 ExerciseBlankDTO들도 검사해라!
    private List<ExerciseBlankDTO> blanks;
}