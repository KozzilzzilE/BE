package com.pocketco.domain.user.dto;

public class UserRequestDTO {
    public record UpdateLanguageRequest(String language) {}
    public record UpdateNicknameRequest(String nickname) {}
}
