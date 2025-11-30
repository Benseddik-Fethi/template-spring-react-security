package com.company.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application Spring Boot.
 * <p>
 * Point d'entrée de l'application backend qui configure et démarre
 * le contexte Spring avec tous les composants auto-configurés.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@SpringBootApplication
public class BackendApplication {

    /**
     * Point d'entrée principal de l'application.
     *
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
