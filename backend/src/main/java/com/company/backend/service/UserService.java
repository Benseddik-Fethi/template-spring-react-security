package com.company.backend.service;

import com.company.backend.dto.request.ChangePasswordRequest;
import com.company.backend.dto.request.ForgotPasswordRequest;
import com.company.backend.dto.request.ResendVerificationRequest;
import com.company.backend.dto.request.ResetPasswordRequest;
import com.company.backend.dto.response.UserResponse;

import java.util.UUID;

/**
 * Interface du service de gestion des utilisateurs.
 * <p>
 * Gère toutes les opérations liées aux comptes utilisateur :
 * vérification d'email, réinitialisation et changement de mot de passe,
 * gestion du profil.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public interface UserService {

    /**
     * Envoie un email de vérification à l'utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     */
    void sendVerificationEmail(UUID userId);

    /**
     * Renvoie l'email de vérification à un utilisateur.
     *
     * @param request l'email de l'utilisateur
     */
    void resendVerificationEmail(ResendVerificationRequest request);

    /**
     * Vérifie l'adresse email avec le token reçu.
     *
     * @param token le token de vérification
     * @return {@code true} si la vérification a réussi
     */
    boolean verifyEmail(String token);

    /**
     * Initie une demande de réinitialisation de mot de passe.
     *
     * @param request l'email de l'utilisateur
     */
    void forgotPassword(ForgotPasswordRequest request);

    /**
     * Vérifie si un token de réinitialisation est valide.
     *
     * @param token le token à vérifier
     * @return {@code true} si le token est valide
     */
    boolean isResetTokenValid(String token);

    /**
     * Réinitialise le mot de passe avec un token valide.
     *
     * @param request le token et le nouveau mot de passe
     */
    void resetPassword(ResetPasswordRequest request);

    /**
     * Change le mot de passe d'un utilisateur connecté.
     *
     * @param userId  l'identifiant de l'utilisateur
     * @param request le mot de passe actuel et le nouveau
     */
    void changePassword(UUID userId, ChangePasswordRequest request);

    /**
     * Récupère les informations d'un utilisateur par son identifiant.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return les informations de l'utilisateur
     */
    UserResponse getUserById(UUID userId);

    /**
     * Récupère les informations d'un utilisateur par son email.
     *
     * @param email l'adresse email de l'utilisateur
     * @return les informations de l'utilisateur
     */
    UserResponse getUserByEmail(String email);

    /**
     * Met à jour la langue préférée de l'utilisateur.
     *
     * @param userId   l'identifiant de l'utilisateur
     * @param language la nouvelle langue préférée (fr ou en)
     */
    void updateLanguage(UUID userId, String language);

    /**
     * Récupère la langue préférée de l'utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return la langue préférée de l'utilisateur
     */
    String getLanguage(UUID userId);
}