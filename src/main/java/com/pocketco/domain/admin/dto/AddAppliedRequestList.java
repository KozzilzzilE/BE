package com.pocketco.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AddAppliedRequestList(
        @Schema(description = "해당 페이지에 필요한 예제 코드들")
        @Valid
        @NotEmpty
        List<AddAppliedRequest> requests
) {}
