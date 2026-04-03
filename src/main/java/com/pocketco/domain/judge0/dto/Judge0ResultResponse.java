package com.pocketco.domain.judge0.dto;

import com.pocketco.domain.user.entity.HistoryStatus;
import lombok.Builder;

@Builder
public record Judge0ResultResponse(
        boolean allDone,
        HistoryStatus newStatus
) { }