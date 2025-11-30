package com.company.backend.service.impl;

import com.company.backend.domain.AuditLog;
import com.company.backend.domain.PasswordResetToken;
import com.company.backend.domain.User;
import com.company.backend.domain.VerificationToken;
import com.company.backend.dto.request.ChangePasswordRequest;
import com.company.backend.dto.request.ForgotPasswordRequest;
import com.company.backend.dto.request.ResendVerificationRequest;
import com.company.backend.dto.request.ResetPasswordRequest;
import com.company.backend.dto.response.UserResponse;
import com.company.backend.exception.BadRequestException;
import com.company.backend.exception.ResourceNotFoundException;
import com.company.backend.repository.*;
import com.company.backend.service.EmailService;
import com.company.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Implémentation du service de gestion des utilisateurs.
 * <p>
 * Gère la vérification d'email, la réinitialisation et le changement
 * de mot de passe, ainsi que la consultation du profil utilisateur.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final SessionRepository sessionRepository;
    private final AuditLogRepository auditLogRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Override
    public void sendVerificationEmail(UUID userId) {
        User user = findUserById(userId);

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            log.debug("Email déjà vérifié pour: {}", user.getEmail());
            return;
        }

        verificationTokenRepository.deleteByUserId(userId);

        VerificationToken token = VerificationToken.create(user);
        verificationTokenRepository.save(token);

        String verificationLink = buildVerificationLink(token.getToken());
        emailService.sendVerificationEmail(
                user.getEmail(),
                user.getFirstName() != null ? user.getFirstName() : "Utilisateur",
                verificationLink
        );

        log.info("Email de vérification envoyé à: {}", user.getEmail());
    }

    @Override
    public void resendVerificationEmail(ResendVerificationRequest request) {
        userRepository.findByEmail(request.email())
                .filter(user -> !Boolean.TRUE.equals(user.getEmailVerified()))
                .ifPresent(user -> sendVerificationEmail(user.getId()));

        log.debug("Demande de renvoi de vérification pour: {}", request.email());
    }

    @Override
    public boolean verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository
                .findValidByToken(token, Instant.now())
                .orElse(null);

        if (verificationToken == null) {
            log.warn("Token de vérification invalide, expiré ou déjà utilisé");
            return false;
        }

        User user = verificationToken.getUser();
        UUID userId = user.getId();

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            verificationTokenRepository.deleteByUserId(userId);
            return true;
        }

        user.setEmailVerified(true);
        userRepository.save(user);

        verificationTokenRepository.deleteByUserId(userId);

        try {
            emailService.sendWelcomeEmail(
                    user.getEmail(),
                    user.getFirstName() != null ? user.getFirstName() : "Utilisateur"
            );
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email de bienvenue", e);
        }

        log.info("Email vérifié avec succès pour: {}", user.getEmail());
        return true;
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            passwordResetTokenRepository.invalidateAllUserTokens(user.getId());

            PasswordResetToken token = PasswordResetToken.create(user);
            passwordResetTokenRepository.save(token);

            String resetLink = buildPasswordResetLink(token.getToken());
            emailService.sendPasswordResetEmail(
                    user.getEmail(),
                    user.getFirstName() != null ? user.getFirstName() : "Utilisateur",
                    resetLink
            );

            log.info("Email de réinitialisation envoyé à: {}", user.getEmail());
        });

        log.debug("Demande de réinitialisation pour: {}", request.email());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isResetTokenValid(String token) {
        return passwordResetTokenRepository
                .findValidByToken(token, Instant.now())
                .isPresent();
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository
                .findValidByToken(request.token(), Instant.now())
                .orElseThrow(() -> new BadRequestException("Token invalide ou expiré"));

        User user = resetToken.getUser();

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        resetToken.markAsUsed();
        passwordResetTokenRepository.save(resetToken);

        sessionRepository.revokeAllUserSessions(user.getId(), Instant.now());

        emailService.sendPasswordChangedEmail(
                user.getEmail(),
                user.getFirstName() != null ? user.getFirstName() : "Utilisateur"
        );

        auditLogRepository.save(AuditLog.passwordChanged(user, "password_reset"));

        log.info("Mot de passe réinitialisé pour: {}", user.getEmail());
    }

    @Override
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        User user = findUserById(userId);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Mot de passe actuel incorrect");
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Le nouveau mot de passe doit être différent de l'ancien");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        emailService.sendPasswordChangedEmail(
                user.getEmail(),
                user.getFirstName() != null ? user.getFirstName() : "Utilisateur"
        );

        auditLogRepository.save(AuditLog.passwordChanged(user, "user_change"));

        log.info("Mot de passe changé pour: {}", user.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID userId) {
        User user = findUserById(userId);
        return UserResponse.fromEntity(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return UserResponse.fromEntity(user);
    }

    @Override
    public void updateLanguage(UUID userId, String language) {
        User user = findUserById(userId);
        user.setPreferredLanguage(language);
        userRepository.save(user);
        log.info("Language preference updated for user {}: {}", userId, language);
    }

    @Override
    @Transactional(readOnly = true)
    public String getLanguage(UUID userId) {
        User user = findUserById(userId);
        return user.getPreferredLanguage();
    }

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return l'utilisateur trouvé
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas
     */
    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    /**
     * Construit le lien de vérification d'email.
     *
     * @param token le token de vérification
     * @return l'URL complète de vérification
     */
    private String buildVerificationLink(String token) {
        return frontendUrl + "/auth/verify-email?token=" + token;
    }

    /**
     * Construit le lien de réinitialisation de mot de passe.
     *
     * @param token le token de réinitialisation
     * @return l'URL complète de réinitialisation
     */
    private String buildPasswordResetLink(String token) {
        return frontendUrl + "/auth/reset-password?token=" + token;
    }
}