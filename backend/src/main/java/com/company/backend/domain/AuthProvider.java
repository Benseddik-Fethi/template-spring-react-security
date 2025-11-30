package com.company.backend.domain;

/**
 * Énumération des fournisseurs d'authentification supportés.
 * <p>
 * Représente les différentes méthodes d'authentification disponibles
 * pour les utilisateurs de l'application : authentification locale
 * par email/mot de passe ou via des fournisseurs OAuth2 externes.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public enum AuthProvider {

    /**
     * Authentification classique par email et mot de passe.
     */
    EMAIL,

    /**
     * Authentification OAuth2 via Google.
     */
    GOOGLE,

    /**
     * Authentification OAuth2 via Facebook.
     */
    FACEBOOK
}