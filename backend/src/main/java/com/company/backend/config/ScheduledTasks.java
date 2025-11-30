package com.company.backend.config;

import com.company.backend.repository.PasswordResetTokenRepository;
import com.company.backend.repository.SessionRepository;
import com.company.backend.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Configuration des tâches planifiées pour la sécurité et la maintenance.
 * <p>
 * Exécute des opérations périodiques de nettoyage :
 * <ul>
 *   <li>Sessions expirées (tokens JWT refresh)</li>
 *   <li>Tokens de vérification email expirés</li>
 *   <li>Tokens de réinitialisation de mot de passe expirés</li>
 * </ul>
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final SessionRepository sessionRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    /**
     * Nettoie les sessions expirées quotidiennement.
     * <p>
     * Exécution : tous les jours à 2h00 du matin (heure serveur).
     * </p>
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void cleanupExpiredSessions() {
        log.info("Démarrage du nettoyage des sessions expirées...");

        try {
            int deletedCount = sessionRepository.deleteExpiredSessions(Instant.now());

            if (deletedCount > 0) {
                log.info("Sessions expirées supprimées: {}", deletedCount);
            } else {
                log.debug("Aucune session expirée à supprimer");
            }
        } catch (Exception e) {
            log.error("Erreur lors du nettoyage des sessions expirées", e);
        }
    }

    /**
     * Nettoie les sessions révoquées anciennes hebdomadairement.
     * <p>
     * Exécution : tous les dimanches à 3h00 du matin.
     * Les sessions révoquées sont conservées 30 jours pour audit/forensics.
     * </p>
     */
    @Scheduled(cron = "0 0 3 * * SUN")
    @Transactional
    public void cleanupOldRevokedSessions() {
        log.info("Démarrage du nettoyage des sessions révoquées anciennes...");

        try {
            Instant thirtyDaysAgo = Instant.now().minusSeconds(30L * 24 * 60 * 60);
            int deletedCount = sessionRepository.deleteRevokedSessionsOlderThan(thirtyDaysAgo);

            if (deletedCount > 0) {
                log.info("Sessions révoquées anciennes supprimées: {}", deletedCount);
            } else {
                log.debug("Aucune session révoquée ancienne à supprimer");
            }
        } catch (Exception e) {
            log.error("Erreur lors du nettoyage des sessions révoquées", e);
        }
    }

    /**
     * Nettoie les tokens de vérification email expirés quotidiennement.
     * <p>
     * Exécution : tous les jours à 1h30 du matin.
     * </p>
     */
    @Scheduled(cron = "0 30 1 * * ?")
    @Transactional
    public void cleanupExpiredVerificationTokens() {
        log.info("Démarrage du nettoyage des tokens de vérification expirés...");

        try {
            int deletedCount = verificationTokenRepository.deleteExpiredTokens(Instant.now());

            if (deletedCount > 0) {
                log.info("Tokens de vérification expirés supprimés: {}", deletedCount);
            } else {
                log.debug("Aucun token de vérification expiré à supprimer");
            }
        } catch (Exception e) {
            log.error("Erreur lors du nettoyage des tokens de vérification", e);
        }
    }

    /**
     * Nettoie les tokens de réinitialisation de mot de passe quotidiennement.
     * <p>
     * Exécution : tous les jours à 1h45 du matin.
     * Supprime les tokens expirés et les tokens déjà utilisés.
     * </p>
     */
    @Scheduled(cron = "0 45 1 * * ?")
    @Transactional
    public void cleanupExpiredPasswordResetTokens() {
        log.info("Démarrage du nettoyage des tokens de reset de mot de passe...");

        try {
            int expiredCount = passwordResetTokenRepository.deleteExpiredTokens(Instant.now());
            int usedCount = passwordResetTokenRepository.deleteUsedTokens();

            int totalDeleted = expiredCount + usedCount;
            if (totalDeleted > 0) {
                log.info("Tokens de reset supprimés: {} (expirés: {}, utilisés: {})",
                        totalDeleted, expiredCount, usedCount);
            } else {
                log.debug("Aucun token de reset à supprimer");
            }
        } catch (Exception e) {
            log.error("Erreur lors du nettoyage des tokens de reset", e);
        }
    }
}
