package com.company.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de requête pour la réinitialisation du mot de passe avec un token.
 * <p>
 * Utilisé lorsqu'un utilisateur clique sur le lien de réinitialisation
 * reçu par email. Le token valide la demande et le nouveau mot de passe
 * doit respecter les critères de sécurité stricts.
 * </p>
 *
 * @param token       le token de réinitialisation reçu par email
 * @param newPassword le nouveau mot de passe souhaité
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public record ResetPasswordRequest(
        @NotBlank(message = "Le token est obligatoire")
        String token,

        @NotBlank(message = "Le nouveau mot de passe est obligatoire")
        @Size(min = 12, max = 128, message = "Le mot de passe doit contenir entre 12 et 128 caractères")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^()_+\\-=\\[\\]{};':\"\\\\|,.<>/`~]).*$",
                message = "Le mot de passe doit contenir : 1 minuscule, 1 majuscule, 1 chiffre et 1 caractère spécial"
        )
        String newPassword
) {
}