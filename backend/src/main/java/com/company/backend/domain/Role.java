package com.company.backend.domain;

/**
 * Énumération des rôles utilisateur dans l'application.
 * <p>
 * Définit les différents niveaux d'accès et de permissions
 * disponibles pour les utilisateurs de l'application.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public enum Role {

    /**
     * Rôle utilisateur standard avec accès limité aux fonctionnalités de base.
     */
    USER,

    /**
     * Rôle administrateur avec accès complet à toutes les fonctionnalités.
     */
    ADMIN
}
