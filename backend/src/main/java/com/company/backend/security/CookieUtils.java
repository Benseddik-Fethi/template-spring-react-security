package com.company.templatespringreactsecurity.security;

import com.company.templatespringreactsecurity.config.JwtProperties;
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
 *
 * 🛡️ Sécurité :
 * - HTTP-only : Inaccessible au JavaScript (protection XSS)
 * - Secure : HTTPS uniquement en production
 * - SameSite=Strict : Protection CSRF
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

    // ✅ CORRECTION ICI : "Lax" est nécessaire pour que le cookie survive à la redirection OAuth2
    @Value("${app.security.cookie.same-site:Lax}")
    private String sameSite;

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        int maxAge = (int) jwtProperties.refreshToken().expiration().toSeconds();
        addHttpOnlyCookie(response, REFRESH_TOKEN_COOKIE, refreshToken, "/api/v1/auth", maxAge);
    }

    public void addAccessTokenCookie(HttpServletResponse response, String accessToken) {
        int maxAge = (int) jwtProperties.accessToken().expiration().toSeconds();
        addHttpOnlyCookie(response, ACCESS_TOKEN_COOKIE, accessToken, "/", maxAge);
    }

    public void clearAuthCookies(HttpServletResponse response) {
        deleteCookie(response, REFRESH_TOKEN_COOKIE, "/api/v1/auth");
        deleteCookie(response, ACCESS_TOKEN_COOKIE, "/");
    }

    public Optional<String> getRefreshTokenFromCookie(HttpServletRequest request) {
        return getCookieValue(request, REFRESH_TOKEN_COOKIE);
    }

    public Optional<String> getAccessTokenFromCookie(HttpServletRequest request) {
        return getCookieValue(request, ACCESS_TOKEN_COOKIE);
    }

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