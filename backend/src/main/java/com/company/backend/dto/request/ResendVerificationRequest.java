package com.company.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO de requête pour demander le renvoi de l'email de vérification.
 * <p>
 * Permet à un utilisateur de recevoir à nouveau l'email de vérification
 * si le premier n'a pas été reçu ou a expiré.
 * </p>
 *
 * @param email l'adresse email du compte à vérifier
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public record ResendVerificationRequest(
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        String email
) {
}