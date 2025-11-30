package com.company.backend.repository;

import com.company.backend.domain.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Repository pour l'accès aux logs d'audit.
 * <p>
 * Permet la recherche et le comptage des actions enregistrées
 * dans le journal d'audit pour la traçabilité et la sécurité.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    /**
     * Recherche les logs d'un utilisateur avec pagination.
     *
     * @param userId   l'identifiant de l'utilisateur
     * @param pageable les paramètres de pagination
     * @return la page de logs
     */
    Page<AuditLog> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    /**
     * Recherche les logs par type d'action.
     *
     * @param action le type d'action
     * @return la liste des logs correspondants
     */
    List<AuditLog> findByActionOrderByCreatedAtDesc(String action);

    /**
     * Recherche les logs entre deux dates.
     *
     * @param start la date de début
     * @param end   la date de fin
     * @return la liste des logs correspondants
     */
    List<AuditLog> findByCreatedAtBetweenOrderByCreatedAtDesc(Instant start, Instant end);

    /**
     * Recherche les logs d'un utilisateur par type d'action.
     *
     * @param userId l'identifiant de l'utilisateur
     * @param action le type d'action
     * @return la liste des logs correspondants
     */
    List<AuditLog> findByUserIdAndActionOrderByCreatedAtDesc(UUID userId, String action);

    /**
     * Compte les tentatives de connexion échouées récentes pour une IP.
     *
     * @param ipAddress l'adresse IP
     * @param action    le type d'action (LOGIN_FAILED)
     * @param after     la date à partir de laquelle compter
     * @return le nombre de tentatives
     */
    long countByIpAddressAndActionAndCreatedAtAfter(String ipAddress, String action, Instant after);
}