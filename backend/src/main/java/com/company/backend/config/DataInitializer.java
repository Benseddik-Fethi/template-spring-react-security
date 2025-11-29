package com.company.templatespringreactsecurity.config;

import com.company.templatespringreactsecurity.domain.AuthProvider;
import com.company.templatespringreactsecurity.domain.Role;
import com.company.templatespringreactsecurity.domain.User;
import com.company.templatespringreactsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initialisation des données de test au démarrage.
 * Ne s'active que si le profil n'est PAS "prod" (sécurité).
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Profile("!prod") // Ne jamais exécuter en production
    public CommandLineRunner initData() {
        return args -> {
            if (userRepository.count() > 0) {
                log.info("🚫 La base de données contient déjà des utilisateurs. Initialisation ignorée.");
                return;
            }

            log.info("🚀 Initialisation du jeu de données de démarrage...");

            // 1. Créer un ADMIN
            createAccount(
                    "admin@template.com",
                    "Password123!",
                    "Admin",
                    "System",
                    Role.ADMIN
            );

            // 2. Créer un USER standard
            createAccount(
                    "user@template.com",
                    "Password123!",
                    "Jean",
                    "Dupont",
                    Role.USER
            );

            log.info("✅ Jeu de données initialisé avec succès !");
            log.info("👉 Admin: admin@template.com / Password123!");
            log.info("👉 User:  user@template.com  / Password123!");
        };
    }

    private void createAccount(String email, String password, String firstName, String lastName, Role role) {
        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .provider(AuthProvider.EMAIL)
                .emailVerified(true) // Compte déjà activé
                .failedLoginAttempts(0)
                .build();

        userRepository.save(user);
    }
}