package com.pocketco.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRequestDTO {
    public record UpdateLanguageRequest(@NotBlank String language) {}
    public record UpdateNicknameRequest(@NotBlank String nickname) {}
}
