package com.pocketco.domain.judge0.dto;

public record CodeSubmitRequest(
        String sourceCode,
        Double timeLimit,
        Integer memoryLimit
) { }