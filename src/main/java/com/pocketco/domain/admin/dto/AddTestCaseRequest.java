package com.pocketco.domain.admin.dto;

public record AddTestCaseRequest(
        String input,
        String output
) {}