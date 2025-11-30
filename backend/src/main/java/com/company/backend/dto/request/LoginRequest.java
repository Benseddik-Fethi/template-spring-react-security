package com.company.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO de requête pour la connexion d'un utilisateur.
 * <p>
 * Contient les informations d'identification nécessaires pour
 * authentifier un utilisateur via email et mot de passe.
 * </p>
 *
 * @param email    l'adresse email de l'utilisateur
 * @param password le mot de passe de l'utilisateur
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public record LoginRequest(
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password
) {
}