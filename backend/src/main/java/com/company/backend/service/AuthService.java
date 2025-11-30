package com.company.backend.service;

import com.company.backend.dto.request.LoginRequest;
import com.company.backend.dto.request.OAuthCodeExchangeRequest;
import com.company.backend.dto.request.RefreshTokenRequest;
import com.company.backend.dto.request.RegisterRequest;
import com.company.backend.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface du service d'authentification.
 * <p>
 * Gère toutes les opérations liées à l'authentification des utilisateurs :
 * inscription, connexion, déconnexion, rafraîchissement des tokens et
 * authentification OAuth2.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public interface AuthService {

    /**
     * Inscrit un nouvel utilisateur.
     *
     * @param request     les informations d'inscription
     * @param httpRequest la requête HTTP pour les métadonnées
     */
    void register(RegisterRequest request, HttpServletRequest httpRequest);

    /**
     * Authentifie un utilisateur et génère les tokens JWT.
     *
     * @param request     les identifiants de connexion
     * @param httpRequest la requête HTTP pour les métadonnées
     * @return la réponse d'authentification avec les tokens
     */
    AuthResponse login(LoginRequest request, HttpServletRequest httpRequest);

    /**
     * Rafraîchit les tokens d'authentification.
     *
     * @param request     le refresh token
     * @param httpRequest la requête HTTP pour les métadonnées
     * @return la nouvelle réponse d'authentification
     */
    AuthResponse refreshToken(RefreshTokenRequest request, HttpServletRequest httpRequest);

    /**
     * Échange un code d'autorisation OAuth2 contre les tokens.
     *
     * @param request le code d'autorisation
     * @return la réponse d'authentification avec les tokens
     */
    AuthResponse exchangeOAuthCode(OAuthCodeExchangeRequest request);

    /**
     * Déconnecte l'utilisateur en révoquant sa session.
     *
     * @param refreshToken le refresh token à révoquer
     * @param httpRequest  la requête HTTP pour les métadonnées
     */
    void logout(String refreshToken, HttpServletRequest httpRequest);

    /**
     * Déconnecte l'utilisateur de toutes ses sessions.
     *
     * @param httpRequest la requête HTTP pour les métadonnées
     */
    void logoutAll(HttpServletRequest httpRequest);
}