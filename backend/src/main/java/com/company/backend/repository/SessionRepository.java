package com.company.backend.repository;

import com.company.backend.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'accès aux sessions utilisateur (refresh tokens).
 * <p>
 * Gère les sessions utilisateur contenant les refresh tokens hashés.
 * Utilise des JOIN FETCH pour optimiser le chargement des utilisateurs.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

    /**
     * Recherche une session active par hash du refresh token.
     *
     * @param tokenHash le hash SHA-256 du refresh token
     * @param now       l'instant actuel pour vérifier l'expiration
     * @return la session si elle est valide
     */
    @Query("""
                SELECT s FROM Session s 
                JOIN FETCH s.user
                WHERE s.refreshTokenHash = :tokenHash 
                AND s.revokedAt IS NULL 
                AND s.expiresAt > :now
            """)
    Optional<Session> findValidByRefreshTokenHash(String tokenHash, Instant now);

    /**
     * Recherche toutes les sessions actives d'un utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     * @param now    l'instant actuel
     * @return la liste des sessions actives
     */
    @Query("""
                SELECT s FROM Session s 
                WHERE s.user.id = :userId 
                AND s.revokedAt IS NULL 
                AND s.expiresAt > :now 
                ORDER BY s.createdAt DESC
            """)
    List<Session> findActiveSessionsByUserId(UUID userId, Instant now);

    /**
     * Révoque toutes les sessions d'un utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     * @param now    l'instant de révocation
     * @return le nombre de sessions révoquées
     */
    @Modifying
    @Query("UPDATE Session s SET s.revokedAt = :now WHERE s.user.id = :userId AND s.revokedAt IS NULL")
    int revokeAllUserSessions(UUID userId, Instant now);

    /**
     * Révoque une session spécifique.
     *
     * @param sessionId l'identifiant de la session
     * @param now       l'instant de révocation
     * @return le nombre de sessions révoquées
     */
    @Modifying
    @Query("UPDATE Session s SET s.revokedAt = :now WHERE s.id = :sessionId AND s.revokedAt IS NULL")
    int revokeSession(UUID sessionId, Instant now);

    /**
     * Supprime les sessions expirées (nettoyage périodique).
     *
     * @param now l'instant actuel
     * @return le nombre de sessions supprimées
     */
    @Modifying
    @Query("DELETE FROM Session s WHERE s.expiresAt < :now")
    int deleteExpiredSessions(Instant now);

    /**
     * Supprime les sessions révoquées anciennes.
     * <p>
     * Les sessions révoquées sont conservées 30 jours pour audit/forensics.
     * </p>
     *
     * @param before la date limite
     * @return le nombre de sessions supprimées
     */
    @Modifying
    @Query("DELETE FROM Session s WHERE s.revokedAt IS NOT NULL AND s.revokedAt < :before")
    int deleteRevokedSessionsOlderThan(Instant before);

    /**
     * Compte les sessions actives d'un utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     * @param now    l'instant actuel
     * @return le nombre de sessions actives
     */
    @Query("""
                SELECT COUNT(s) FROM Session s 
                WHERE s.user.id = :userId 
                AND s.revokedAt IS NULL 
                AND s.expiresAt > :now
            """)
    long countActiveSessionsByUserId(UUID userId, Instant now);
}