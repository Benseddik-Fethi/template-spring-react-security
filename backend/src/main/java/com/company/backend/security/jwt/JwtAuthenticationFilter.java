package com.company.backend.security.jwt;

import com.company.backend.security.CookieUtils;
import com.company.backend.security.CustomUserDetailsService;
import com.company.backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtre JWT pour intercepter et valider les tokens d'authentification.
 * <p>
 * Exécuté une seule fois par requête pour extraire et valider le token JWT,
 * puis configurer le contexte de sécurité Spring si l'authentification réussit.
 * Le token est lu depuis le cookie HTTP-only ou le header Authorization.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final CookieUtils cookieUtils;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = extractTokenFromRequest(request);

            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateWithToken(token, request);
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'authentification JWT: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrait le token JWT de la requête.
     * <p>
     * Priorité de lecture :
     * <ol>
     *   <li>Cookie HTTP-only (plus sécurisé)</li>
     *   <li>Header Authorization Bearer (fallback pour clients sans cookies)</li>
     * </ol>
     * </p>
     *
     * @param request la requête HTTP
     * @return le token JWT ou null
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String token = cookieUtils.getAccessTokenFromCookie(request).orElse(null);

        if (token != null) {
            log.trace("Token extrait du cookie");
            return token;
        }

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            log.trace("Token extrait du header Authorization");
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    /**
     * Authentifie l'utilisateur à partir du token JWT valide.
     *
     * @param token   le token JWT
     * @param request la requête HTTP
     */
    private void authenticateWithToken(String token, HttpServletRequest request) {
        if (!jwtService.isTokenValid(token)) {
            log.debug("Token JWT invalide");
            return;
        }

        if (!jwtService.isAccessToken(token)) {
            log.debug("Le token n'est pas un access token");
            return;
        }

        UUID userId = jwtService.extractUserId(token).orElse(null);
        if (userId == null) {
            log.debug("Impossible d'extraire l'ID utilisateur du token");
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserById(userId);

        if (!userDetails.isAccountNonLocked()) {
            log.debug("Compte utilisateur verrouillé: {}", userId);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.debug("Utilisateur authentifié via JWT: {}", userId);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/v1/auth/login") ||
                path.startsWith("/api/v1/auth/register") ||
                path.startsWith("/api/v1/auth/refresh") ||
                path.startsWith("/actuator/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs");
    }
}