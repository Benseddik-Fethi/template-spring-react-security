package com.company.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de requête pour le changement de mot de passe d'un utilisateur connecté.
 * <p>
 * Requiert le mot de passe actuel pour validation et un nouveau mot de passe
 * respectant les critères de sécurité : minimum 12 caractères avec au moins
 * une minuscule, une majuscule, un chiffre et un caractère spécial.
 * </p>
 *
 * @param currentPassword le mot de passe actuel de l'utilisateur
 * @param newPassword     le nouveau mot de passe souhaité
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public record ChangePasswordRequest(
        @NotBlank(message = "Le mot de passe actuel est obligatoire")
        String currentPassword,

        @NotBlank(message = "Le nouveau mot de passe est obligatoire")
        @Size(min = 12, max = 128, message = "Le mot de passe doit contenir entre 12 et 128 caractères")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^()_+\\-=\\[\\]{};':\"\\\\|,.<>/`~]).*$",
                message = "Le mot de passe doit contenir : 1 minuscule, 1 majuscule, 1 chiffre et 1 caractère spécial"
        )
        String newPassword
) {
}