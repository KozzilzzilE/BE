package com.pocketco.domain.admin.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AddProblemRequests(
        @Valid
        @NotEmpty
        List<AddProblemRequest> requests
) { }