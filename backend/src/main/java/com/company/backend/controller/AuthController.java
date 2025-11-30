package com.company.backend.controller;

import com.company.backend.dto.request.LoginRequest;
import com.company.backend.dto.request.OAuthCodeExchangeRequest;
import com.company.backend.dto.request.RefreshTokenRequest;
import com.company.backend.dto.request.RegisterRequest;
import com.company.backend.dto.response.AuthResponse;
import com.company.backend.dto.response.UserResponse;
import com.company.backend.security.CookieUtils;
import com.company.backend.security.CustomUserDetails;
import com.company.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur REST pour l'authentification.
 * <p>
 * Expose les endpoints d'inscription, connexion, déconnexion,
 * rafraîchissement des tokens et récupération de l'utilisateur courant.
 * Les tokens sont stockés dans des cookies HTTP-only pour la sécurité.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieUtils cookieUtils;

    /**
     * Inscrit un nouvel utilisateur.
     *
     * @param request     les informations d'inscription
     * @param httpRequest la requête HTTP
     * @return un message de confirmation
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest
    ) {
        authService.register(request, httpRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Compte créé avec succès. Veuillez vérifier vos emails."
        ));
    }

    /**
     * Authentifie un utilisateur.
     *
     * @param request      les identifiants de connexion
     * @param httpRequest  la requête HTTP
     * @param httpResponse la réponse HTTP pour les cookies
     * @return la réponse d'authentification avec les tokens
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        AuthResponse response = authService.login(request, httpRequest);
        cookieUtils.addAccessTokenCookie(httpResponse, response.accessToken());
        cookieUtils.addRefreshTokenCookie(httpResponse, response.refreshToken());
        return ResponseEntity.ok(response);
    }

    /**
     * Rafraîchit les tokens d'authentification.
     *
     * @param request      le refresh token (optionnel si cookie présent)
     * @param httpRequest  la requête HTTP
     * @param httpResponse la réponse HTTP pour les cookies
     * @return la nouvelle réponse d'authentification
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody(required = false) RefreshTokenRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        String refreshToken = cookieUtils.getRefreshTokenFromCookie(httpRequest)
                .orElseGet(() -> request != null ? request.refreshToken() : null);

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AuthResponse response = authService.refreshToken(new RefreshTokenRequest(refreshToken), httpRequest);
        cookieUtils.addAccessTokenCookie(httpResponse, response.accessToken());
        cookieUtils.addRefreshTokenCookie(httpResponse, response.refreshToken());
        return ResponseEntity.ok(response);
    }

    /**
     * Échange un code d'autorisation OAuth2 contre les tokens.
     *
     * @param request      le code d'autorisation
     * @param httpResponse la réponse HTTP pour les cookies
     * @return la réponse d'authentification
     */
    @PostMapping("/oauth/exchange")
    public ResponseEntity<AuthResponse> exchangeOAuthCode(
            @Valid @RequestBody OAuthCodeExchangeRequest request,
            HttpServletResponse httpResponse
    ) {
        AuthResponse response = authService.exchangeOAuthCode(request);
        cookieUtils.addAccessTokenCookie(httpResponse, response.accessToken());
        cookieUtils.addRefreshTokenCookie(httpResponse, response.refreshToken());
        return ResponseEntity.ok(response);
    }

    /**
     * Déconnecte l'utilisateur de la session courante.
     *
     * @param request      le refresh token (optionnel si cookie présent)
     * @param httpRequest  la requête HTTP
     * @param httpResponse la réponse HTTP pour supprimer les cookies
     * @return un message de confirmation
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            @RequestBody(required = false) RefreshTokenRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        String refreshToken = cookieUtils.getRefreshTokenFromCookie(httpRequest)
                .orElseGet(() -> request != null ? request.refreshToken() : null);

        authService.logout(refreshToken, httpRequest);
        cookieUtils.clearAuthCookies(httpResponse);
        return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
    }

    /**
     * Déconnecte l'utilisateur de toutes ses sessions.
     *
     * @param httpRequest  la requête HTTP
     * @param httpResponse la réponse HTTP pour supprimer les cookies
     * @return un message de confirmation
     */
    @PostMapping("/logout-all")
    public ResponseEntity<Map<String, String>> logoutAll(
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        authService.logoutAll(httpRequest);
        cookieUtils.clearAuthCookies(httpResponse);
        return ResponseEntity.ok(Map.of("message", "Toutes les sessions ont été révoquées"));
    }

    /**
     * Récupère les informations de l'utilisateur authentifié.
     *
     * @param userDetails les détails de l'utilisateur courant
     * @return les informations de l'utilisateur
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(UserResponse.fromEntity(userDetails.getUser()));
    }
}
