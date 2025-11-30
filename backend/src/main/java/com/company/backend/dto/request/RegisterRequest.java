package com.company.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de requête pour l'inscription d'un nouvel utilisateur.
 * <p>
 * Contient les informations nécessaires pour créer un compte utilisateur.
 * Le mot de passe doit respecter des critères de sécurité stricts :
 * minimum 12 caractères avec au moins une minuscule, une majuscule,
 * un chiffre et un caractère spécial.
 * </p>
 *
 * @param email     l'adresse email de l'utilisateur
 * @param password  le mot de passe (12-128 caractères avec complexité)
 * @param firstName le prénom de l'utilisateur (optionnel)
 * @param lastName  le nom de famille de l'utilisateur (optionnel)
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public record RegisterRequest(
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 12, max = 128, message = "Le mot de passe doit contenir entre 12 et 128 caractères")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^()_+\\-=\\[\\]{};':\"\\\\|,.<>/`~])[A-Za-z\\d@$!%*?&#^()_+\\-=\\[\\]{};':\"\\\\|,.<>/`~]{12,}$",
                message = "Le mot de passe doit contenir au minimum : " +
                        "1 minuscule, 1 majuscule, 1 chiffre et 1 caractère spécial (@$!%*?&#^()_+-=[]{}etc.)"
        )
        String password,

        @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
        String firstName,

        @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
        String lastName
) {
}