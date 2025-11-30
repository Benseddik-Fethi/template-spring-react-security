package com.company.backend.security;

import com.company.backend.config.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

/**
 * Utilitaire pour la gestion des cookies d'authentification.
 * <p>
 * Gère les cookies HTTP-only pour les tokens JWT avec les attributs
 * de sécurité appropriés (Secure, SameSite, HttpOnly).
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Component
@RequiredArgsConstructor
public class CookieUtils {

    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";
    public static final String ACCESS_TOKEN_COOKIE = "access_token";

    private final JwtProperties jwtProperties;

    @Value("${app.security.cookie.secure:false}")
    private boolean secureCookie;

    @Value("${app.security.cookie.domain:}")
    private String cookieDomain;

    @Value("${app.security.cookie.same-site:Lax}")
    private String sameSite;

    /**
     * Ajoute le cookie du refresh token à la réponse.
     *
     * @param response     la réponse HTTP
     * @param refreshToken le refresh token à stocker
     */
    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        int maxAge = (int) jwtProperties.refreshToken().expiration().toSeconds();
        addHttpOnlyCookie(response, REFRESH_TOKEN_COOKIE, refreshToken, "/api/v1/auth", maxAge);
    }

    /**
     * Ajoute le cookie de l'access token à la réponse.
     *
     * @param response    la réponse HTTP
     * @param accessToken l'access token à stocker
     */
    public void addAccessTokenCookie(HttpServletResponse response, String accessToken) {
        int maxAge = (int) jwtProperties.accessToken().expiration().toSeconds();
        addHttpOnlyCookie(response, ACCESS_TOKEN_COOKIE, accessToken, "/", maxAge);
    }

    /**
     * Supprime les cookies d'authentification de la réponse.
     *
     * @param response la réponse HTTP
     */
    public void clearAuthCookies(HttpServletResponse response) {
        deleteCookie(response, REFRESH_TOKEN_COOKIE, "/api/v1/auth");
        deleteCookie(response, ACCESS_TOKEN_COOKIE, "/");
    }

    /**
     * Récupère le refresh token depuis les cookies de la requête.
     *
     * @param request la requête HTTP
     * @return le refresh token s'il existe
     */
    public Optional<String> getRefreshTokenFromCookie(HttpServletRequest request) {
        return getCookieValue(request, REFRESH_TOKEN_COOKIE);
    }

    /**
     * Récupère l'access token depuis les cookies de la requête.
     *
     * @param request la requête HTTP
     * @return l'access token s'il existe
     */
    public Optional<String> getAccessTokenFromCookie(HttpServletRequest request) {
        return getCookieValue(request, ACCESS_TOKEN_COOKIE);
    }

    /**
     * Ajoute un cookie HTTP-only avec les attributs de sécurité.
     *
     * @param response la réponse HTTP
     * @param name     le nom du cookie
     * @param value    la valeur du cookie
     * @param path     le chemin du cookie
     * @param maxAge   la durée de vie en secondes
     */
    private void addHttpOnlyCookie(
            HttpServletResponse response,
            String name,
            String value,
            String path,
            int maxAge
    ) {
        StringBuilder cookieBuilder = new StringBuilder();
        cookieBuilder.append(name).append("=").append(value);
        cookieBuilder.append("; HttpOnly");
        cookieBuilder.append("; Path=").append(path);
        cookieBuilder.append("; Max-Age=").append(maxAge);
        cookieBuilder.append("; SameSite=").append(sameSite);

        if (secureCookie) {
            cookieBuilder.append("; Secure");
        }

        if (cookieDomain != null && !cookieDomain.isBlank()) {
            cookieBuilder.append("; Domain=").append(cookieDomain);
        }

        response.addHeader("Set-Cookie", cookieBuilder.toString());
    }

    /**
     * Supprime un cookie en définissant son Max-Age à 0.
     *
     * @param response la réponse HTTP
     * @param name     le nom du cookie
     * @param path     le chemin du cookie
     */
    private void deleteCookie(HttpServletResponse response, String name, String path) {
        StringBuilder cookieBuilder = new StringBuilder();
        cookieBuilder.append(name).append("=");
        cookieBuilder.append("; HttpOnly");
        cookieBuilder.append("; Path=").append(path);
        cookieBuilder.append("; Max-Age=0");
        cookieBuilder.append("; SameSite=").append(sameSite);

        if (secureCookie) {
            cookieBuilder.append("; Secure");
        }

        if (cookieDomain != null && !cookieDomain.isBlank()) {
            cookieBuilder.append("; Domain=").append(cookieDomain);
        }

        response.addHeader("Set-Cookie", cookieBuilder.toString());
    }

    /**
     * Récupère la valeur d'un cookie depuis la requête.
     *
     * @param request la requête HTTP
     * @param name    le nom du cookie
     * @return la valeur du cookie s'il existe
     */
    private Optional<String> getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}