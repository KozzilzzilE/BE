package com.pocketco.domain.judge0.dto;

public record Judge0IndividualRequest(
        String source_code,
        int language_id,
        String stdin,
        String expected_output
) {}