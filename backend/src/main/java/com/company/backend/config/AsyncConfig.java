package com.company.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuration pour activer les opérations asynchrones.
 * <p>
 * Permet l'utilisation de l'annotation @Async pour exécuter des méthodes
 * en arrière-plan, notamment pour l'envoi d'emails non bloquant.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}