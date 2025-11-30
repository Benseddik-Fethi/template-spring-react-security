package com.company.backend.dto.response;

import com.company.backend.domain.Role;
import com.company.backend.domain.User;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO de réponse contenant les informations publiques d'un utilisateur.
 * <p>
 * Exclut les données sensibles comme le hash du mot de passe et les
 * informations de sécurité (tentatives de connexion, verrouillage, etc.).
 * Compatible avec le frontend React utilisant le format camelCase.
 * </p>
 *
 * @param id                l'identifiant unique de l'utilisateur
 * @param email             l'adresse email de l'utilisateur
 * @param firstName         le prénom de l'utilisateur
 * @param lastName          le nom de famille de l'utilisateur
 * @param avatar            l'URL de l'avatar de l'utilisateur
 * @param role              le rôle de l'utilisateur dans l'application
 * @param emailVerified     indique si l'email a été vérifié
 * @param preferredLanguage la langue préférée de l'utilisateur (fr ou en)
 * @param createdAt         la date de création du compte
 * @param updatedAt         la date de dernière modification
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String avatar,
        Role role,
        Boolean emailVerified,
        String preferredLanguage,
        Instant createdAt,
        Instant updatedAt
) {

    /**
     * Construit un UserResponse à partir d'une entité User.
     *
     * @param user l'entité utilisateur source
     * @return le DTO de réponse correspondant
     */
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAvatar(),
                user.getRole(),
                user.getEmailVerified(),
                user.getPreferredLanguage(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}