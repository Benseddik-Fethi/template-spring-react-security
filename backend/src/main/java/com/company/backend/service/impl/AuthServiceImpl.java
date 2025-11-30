package com.company.backend.service.impl;

import com.company.backend.config.JwtProperties;
import com.company.backend.config.SecurityProperties;
import com.company.backend.domain.*;
import com.company.backend.dto.request.LoginRequest;
import com.company.backend.dto.request.OAuthCodeExchangeRequest;
import com.company.backend.dto.request.RefreshTokenRequest;
import com.company.backend.dto.request.RegisterRequest;
import com.company.backend.dto.response.AuthResponse;
import com.company.backend.dto.response.UserResponse;
import com.company.backend.exception.AccountLockedException;
import com.company.backend.exception.AuthenticationException;
import com.company.backend.exception.BadRequestException;
import com.company.backend.repository.*;
import com.company.backend.security.CustomUserDetails;
import com.company.backend.service.AuthService;
import com.company.backend.service.EmailService;
import com.company.backend.service.JwtService;
import com.company.backend.util.IpAddressResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Implémentation du service d'authentification.
 * <p>
 * Gère l'inscription, la connexion, la déconnexion, le rafraîchissement
 * des tokens et l'authentification OAuth2. Inclut la protection contre
 * les attaques par force brute et les timing attacks.
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
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final AuditLogRepository auditLogRepository;
    private final OAuthAuthorizationCodeRepository authorizationCodeRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final JwtProperties jwtProperties;
    private final SecurityProperties securityProperties;
    private final PasswordEncoder passwordEncoder;
    private final IpAddressResolver ipAddressResolver;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Override
    public void register(RegisterRequest request, HttpServletRequest httpRequest) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Un compte existe déjà avec cet email");
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(Role.USER)
                .provider(AuthProvider.EMAIL)
                .emailVerified(false)
                .build();

        user = userRepository.save(user);

        log.info("Nouvel utilisateur inscrit: {}", user.getEmail());

        sendVerificationEmail(user);
    }

    /**
     * Envoie l'email de vérification à l'utilisateur.
     *
     * @param user l'utilisateur à vérifier
     */
    private void sendVerificationEmail(User user) {
        VerificationToken token = VerificationToken.create(user);
        verificationTokenRepository.save(token);

        String verificationLink = frontendUrl + "/auth/verify-email?token=" + token.getToken();

        emailService.sendVerificationEmail(
                user.getEmail(),
                user.getFirstName() != null ? user.getFirstName() : "Utilisateur",
                verificationLink
        );
    }

    @Override
    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        String ip = ipAddressResolver.resolveClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        User user = userRepository.findByEmail(request.email()).orElse(null);

        boolean passwordMatches;
        if (user != null) {
            passwordMatches = passwordEncoder.matches(request.password(), user.getPasswordHash());
        } else {
            passwordEncoder.matches(request.password(), "$argon2id$v=19$m=65536,t=4,p=4$FAKEHASH$FAKEHASH");
            auditLogRepository.save(AuditLog.loginFailed(request.email(), ip, userAgent, "User not found"));
            throw new AuthenticationException("Email ou mot de passe incorrect");
        }

        if (user.isAccountLocked()) {
            throw new AccountLockedException(user.getLockedUntil());
        }

        if (!passwordMatches) {
            handleFailedLogin(user, ip, userAgent);
            throw new AuthenticationException("Email ou mot de passe incorrect");
        }

        if (Boolean.FALSE.equals(user.getEmailVerified())) {
            log.warn("Connexion refusée (Email non vérifié): {}", user.getEmail());
            throw new AuthenticationException("Veuillez vérifier votre adresse email avant de vous connecter.");
        }

        user.resetFailedLoginAttempts();
        userRepository.save(user);
        auditLogRepository.save(AuditLog.loginSuccess(user, ip, userAgent));

        return createAuthResponse(user, httpRequest);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request, HttpServletRequest httpRequest) {
        String refreshToken = request.refreshToken();
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new AuthenticationException("Refresh token invalide");
        }
        if (jwtService.isAccessToken(refreshToken)) {
            throw new AuthenticationException("Token invalide");
        }

        String tokenHash = jwtService.hashToken(refreshToken);
        Session session = sessionRepository.findValidByRefreshTokenHash(tokenHash, Instant.now())
                .orElseThrow(() -> new AuthenticationException("Session invalide"));

        User user = session.getUser();
        session.revoke();
        sessionRepository.save(session);

        return createAuthResponse(user, httpRequest);
    }

    @Override
    public AuthResponse exchangeOAuthCode(OAuthCodeExchangeRequest request) {
        OAuthAuthorizationCode authCode = authorizationCodeRepository
                .findValidByCode(request.code(), Instant.now())
                .orElseThrow(() -> new AuthenticationException("Code invalide"));

        authCode.markAsUsed();
        authorizationCodeRepository.save(authCode);
        User user = authCode.getUser();

        long expiresIn = jwtProperties.accessToken().expiration().getSeconds();
        return new AuthResponse(authCode.getAccessToken(), authCode.getRefreshToken(), expiresIn, UserResponse.fromEntity(user));
    }

    @Override
    public void logout(String refreshToken, HttpServletRequest httpRequest) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return;
        }
        String tokenHash = jwtService.hashToken(refreshToken);
        sessionRepository.findValidByRefreshTokenHash(tokenHash, Instant.now()).ifPresent(s -> {
            s.revoke();
            sessionRepository.save(s);
            auditLogRepository.save(AuditLog.logout(s.getUser(), ipAddressResolver.resolveClientIp(httpRequest)));
        });
    }

    @Override
    public void logoutAll(HttpServletRequest httpRequest) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sessionRepository.revokeAllUserSessions(userDetails.getId(), Instant.now());
    }

    /**
     * Crée la réponse d'authentification avec les tokens JWT.
     *
     * @param user        l'utilisateur authentifié
     * @param httpRequest la requête HTTP
     * @return la réponse d'authentification
     */
    private AuthResponse createAuthResponse(User user, HttpServletRequest httpRequest) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Session session = Session.builder()
                .user(user)
                .refreshTokenHash(jwtService.hashToken(refreshToken))
                .ipAddress(ipAddressResolver.resolveClientIp(httpRequest))
                .userAgent(httpRequest.getHeader("User-Agent"))
                .expiresAt(Instant.now().plus(jwtProperties.refreshToken().expiration()))
                .build();
        sessionRepository.save(session);

        long expiresIn = jwtProperties.accessToken().expiration().getSeconds();
        return new AuthResponse(accessToken, refreshToken, expiresIn, UserResponse.fromEntity(user));
    }

    /**
     * Gère une tentative de connexion échouée.
     *
     * @param user      l'utilisateur concerné
     * @param ip        l'adresse IP du client
     * @param userAgent le User-Agent du navigateur
     */
    private void handleFailedLogin(User user, String ip, String userAgent) {
        SecurityProperties.BruteForce bruteForce = securityProperties.bruteForce();
        user.recordFailedLogin(bruteForce.maxAttempts(), (int) bruteForce.lockDuration().toMinutes());
        userRepository.save(user);
        auditLogRepository.save(AuditLog.loginFailed(user.getEmail(), ip, userAgent, "Invalid password"));
        if (user.isAccountLocked()) {
            auditLogRepository.save(AuditLog.accountLocked(user, ip));
        }
    }
}