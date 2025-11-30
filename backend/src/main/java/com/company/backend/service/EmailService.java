package com.company.backend.service;

/**
 * Interface du service d'envoi d'emails.
 * <p>
 * Gère l'envoi de tous les emails transactionnels de l'application :
 * vérification d'email, réinitialisation de mot de passe, notifications.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public interface EmailService {

    /**
     * Envoie un email de vérification d'adresse.
     *
     * @param to               l'adresse email destinataire
     * @param firstName        le prénom de l'utilisateur
     * @param verificationLink le lien de vérification
     */
    void sendVerificationEmail(String to, String firstName, String verificationLink);

    /**
     * Envoie un email de réinitialisation de mot de passe.
     *
     * @param to        l'adresse email destinataire
     * @param firstName le prénom de l'utilisateur
     * @param resetLink le lien de réinitialisation
     */
    void sendPasswordResetEmail(String to, String firstName, String resetLink);

    /**
     * Envoie un email de confirmation de changement de mot de passe.
     *
     * @param to        l'adresse email destinataire
     * @param firstName le prénom de l'utilisateur
     */
    void sendPasswordChangedEmail(String to, String firstName);

    /**
     * Envoie un email de bienvenue après vérification du compte.
     *
     * @param to        l'adresse email destinataire
     * @param firstName le prénom de l'utilisateur
     */
    void sendWelcomeEmail(String to, String firstName);
}