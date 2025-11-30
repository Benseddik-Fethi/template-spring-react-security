package com.company.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;

/**
 * Exception levée lorsqu'un compte utilisateur est verrouillé.
 * <p>
 * Retourne automatiquement un code HTTP 423 (Locked).
 * Indique la date jusqu'à laquelle le compte est verrouillé.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@ResponseStatus(HttpStatus.LOCKED)
public class AccountLockedException extends RuntimeException {

    private final Instant lockedUntil;

    /**
     * Constructeur avec date de déverrouillage.
     *
     * @param lockedUntil la date jusqu'à laquelle le compte est verrouillé
     */
    public AccountLockedException(Instant lockedUntil) {
        super("Compte verrouillé suite à trop de tentatives de connexion échouées");
        this.lockedUntil = lockedUntil;
    }

    /**
     * Constructeur avec message personnalisé et date de déverrouillage.
     *
     * @param message     le message d'erreur
     * @param lockedUntil la date jusqu'à laquelle le compte est verrouillé
     */
    public AccountLockedException(String message, Instant lockedUntil) {
        super(message);
        this.lockedUntil = lockedUntil;
    }

    public Instant getLockedUntil() {
        return lockedUntil;
    }
}