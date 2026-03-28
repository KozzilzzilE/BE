package com.pocketco.domain.judge0.dto;

import java.util.List;

public record Judge0BatchRequest(
        List<Judge0IndividualRequest> submissions
) {}