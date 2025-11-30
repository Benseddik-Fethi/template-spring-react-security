package com.company.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lors d'un problème d'authentification.
 * <p>
 * Retourne automatiquement un code HTTP 401 (Unauthorized).
 * Utilisée pour les erreurs de login, tokens invalides, etc.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {

    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message d'erreur
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message le message d'erreur
     * @param cause   la cause de l'exception
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}