package com.company.backend.config;

import com.company.backend.domain.AuthProvider;
import com.company.backend.domain.Role;
import com.company.backend.domain.User;
import com.company.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initialisation des données de test au démarrage de l'application.
 * <p>
 * Crée des comptes utilisateur par défaut pour faciliter le développement
 * et les tests. Cette configuration n'est active que si le profil "prod"
 * n'est pas actif, pour des raisons de sécurité.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Initialise les données de test au démarrage.
     * <p>
     * Crée un compte admin et un compte utilisateur standard si la base
     * de données est vide. Ne s'exécute jamais en production.
     * </p>
     *
     * @return le runner d'initialisation
     */
    @Bean
    @Profile("!prod")
    public CommandLineRunner initData() {
        return args -> {
            if (userRepository.count() > 0) {
                log.info("La base de données contient déjà des utilisateurs. Initialisation ignorée.");
                return;
            }

            log.info("Initialisation du jeu de données de démarrage...");

            createAccount(
                    "admin@template.com",
                    "Password123!",
                    "Admin",
                    "System",
                    Role.ADMIN
            );

            createAccount(
                    "user@template.com",
                    "Password123!",
                    "Jean",
                    "Dupont",
                    Role.USER
            );

            log.info("Jeu de données initialisé avec succès !");
            log.info("Admin: admin@template.com / Password123!");
            log.info("User:  user@template.com  / Password123!");
        };
    }

    /**
     * Crée un compte utilisateur avec les paramètres spécifiés.
     *
     * @param email     l'adresse email
     * @param password  le mot de passe en clair
     * @param firstName le prénom
     * @param lastName  le nom de famille
     * @param role      le rôle de l'utilisateur
     */
    private void createAccount(String email, String password, String firstName, String lastName, Role role) {
        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .provider(AuthProvider.EMAIL)
                .emailVerified(true)
                .failedLoginAttempts(0)
                .build();

        userRepository.save(user);
    }
}