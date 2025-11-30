package com.company.backend.repository;

import com.company.backend.domain.OAuthAuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'accès aux codes d'autorisation OAuth2.
 * <p>
 * Gère les codes temporaires générés après authentification OAuth2
 * et échangés contre les tokens JWT.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Repository
public interface OAuthAuthorizationCodeRepository extends JpaRepository<OAuthAuthorizationCode, UUID> {

    /**
     * Recherche un code valide (non expiré et non utilisé).
     *
     * @param code le code à rechercher
     * @param now  l'instant actuel
     * @return le code s'il est valide
     */
    @Query("""
                SELECT c FROM OAuthAuthorizationCode c 
                JOIN FETCH c.user
                WHERE c.code = :code 
                AND c.used = false 
                AND c.expiresAt > :now
            """)
    Optional<OAuthAuthorizationCode> findValidByCode(String code, Instant now);

    /**
     * Supprime les codes expirés (nettoyage périodique).
     *
     * @param now l'instant actuel
     * @return le nombre de codes supprimés
     */
    @Modifying
    @Query("DELETE FROM OAuthAuthorizationCode c WHERE c.expiresAt < :now")
    int deleteExpiredCodes(Instant now);

    /**
     * Supprime les codes déjà utilisés.
     *
     * @return le nombre de codes supprimés
     */
    @Modifying
    @Query("DELETE FROM OAuthAuthorizationCode c WHERE c.used = true")
    int deleteUsedCodes();
}