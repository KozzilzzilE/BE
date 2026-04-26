package com.pocketco.domain.user.dto;

import java.time.LocalDate;

public record MainScreenCalenderDTO(
        LocalDate date,
        Integer count
) { }