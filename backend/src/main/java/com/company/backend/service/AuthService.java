package com.company.backend.service;

import com.company.backend.dto.request.LoginRequest;
import com.company.backend.dto.request.OAuthCodeExchangeRequest;
import com.company.backend.dto.request.RefreshTokenRequest;
import com.company.backend.dto.request.RegisterRequest;
import com.company.backend.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Service d'authentification.
 */
public interface AuthService {

    void register(RegisterRequest request, HttpServletRequest httpRequest);

    AuthResponse login(LoginRequest request, HttpServletRequest httpRequest);

    AuthResponse refreshToken(RefreshTokenRequest request, HttpServletRequest httpRequest);

    AuthResponse exchangeOAuthCode(OAuthCodeExchangeRequest request);

    void logout(String refreshToken, HttpServletRequest httpRequest);

    void logoutAll(HttpServletRequest httpRequest);
}