package com.company.backend.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * Handler d'échec pour l'authentification OAuth2.
 * <p>
 * Redirige vers le frontend avec un message d'erreur générique.
 * Les détails techniques ne sont pas exposés dans l'URL pour des raisons
 * de sécurité (protection contre la fuite d'informations).
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Component
@Slf4j
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${app.security.cors.allowed-origins:http://localhost:5173}")
    private String frontendUrl;

    /**
     * Gère l'échec de l'authentification OAuth2.
     *
     * @param request   la requête HTTP
     * @param response  la réponse HTTP
     * @param exception l'exception d'authentification
     * @throws IOException en cas d'erreur de redirection
     */
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        log.error("OAuth2 authentication failed - Type: {} - Message: {}",
                exception.getClass().getSimpleName(),
                exception.getMessage());
        log.debug("OAuth2 failure details", exception);

        String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/auth/error")
                .queryParam("error", "oauth2_error")
                .queryParam("message", "L'authentification a échoué. Veuillez réessayer.")
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}