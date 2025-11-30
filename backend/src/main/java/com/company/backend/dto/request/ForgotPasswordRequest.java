package com.company.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO de requête pour demander une réinitialisation de mot de passe.
 * <p>
 * L'utilisateur fournit son adresse email et recevra un lien
 * de réinitialisation si un compte existe avec cet email.
 * </p>
 *
 * @param email l'adresse email du compte
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public record ForgotPasswordRequest(
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        String email
) {
}