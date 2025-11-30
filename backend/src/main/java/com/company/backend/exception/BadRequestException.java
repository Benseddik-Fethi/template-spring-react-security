package com.company.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lors d'une requête invalide.
 * <p>
 * Retourne automatiquement un code HTTP 400 (Bad Request).
 * Utilisée pour les données de requête invalides ou manquantes.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message d'erreur
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause.
     *
     * @param message le message d'erreur
     * @param cause   la cause de l'exception
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}