package com.pocketco.domain.auth.application;

import com.pocketco.domain.auth.dto.LoginResponse;
import com.pocketco.domain.auth.dto.SignupRequest;
import com.pocketco.domain.auth.dto.SignupResponse;

public interface AuthService {
    SignupResponse register(SignupRequest request);
    LoginResponse login(String token);
}