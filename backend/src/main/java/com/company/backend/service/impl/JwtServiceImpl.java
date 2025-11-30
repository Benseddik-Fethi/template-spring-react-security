package com.company.backend.service.impl;

import com.company.backend.config.JwtProperties;
import com.company.backend.domain.User;
import com.company.backend.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation du service JWT avec la bibliothèque JJWT.
 * <p>
 * Gère la génération, la validation et l'extraction des claims des tokens JWT.
 * Utilise HMAC-SHA256 pour la signature et stocke les refresh tokens hashés
 * en base de données pour plus de sécurité.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private static final String CLAIM_USER_ID = "uid";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_TYPE = "type";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";

    private static final ThreadLocal<MessageDigest> SHA256_DIGEST = ThreadLocal.withInitial(() -> {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    });

    private final SecretKey secretKey;
    private final JwtProperties jwtProperties;
    private final JwtParser jwtParser;

    /**
     * Constructeur avec injection des propriétés JWT.
     *
     * @param jwtProperties les propriétés de configuration JWT
     */
    public JwtServiceImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(jwtProperties.issuer())
                .requireAudience(jwtProperties.audience())
                .build();
    }

    /**
     * Valide la configuration JWT au démarrage de l'application.
     *
     * @throws IllegalStateException si la configuration est invalide
     */
    @PostConstruct
    public void validateJwtConfiguration() {
        String secret = jwtProperties.secret();

        if (secret.startsWith("CHANGE_ME_IN_PRODUCTION")) {
            throw new IllegalStateException(
                    "SÉCURITÉ CRITIQUE: JWT_SECRET n'est pas configuré! " +
                            "Définissez la variable d'environnement JWT_SECRET avec un secret aléatoire de 512 bits minimum."
            );
        }

        if (secret.length() < 64) {
            throw new IllegalStateException(
                    String.format(
                            "SÉCURITÉ CRITIQUE: JWT_SECRET trop court (%d caractères). " +
                                    "Le secret doit faire au minimum 512 bits (64 caractères). " +
                                    "Générez un secret avec: openssl rand -base64 64",
                            secret.length()
                    )
            );
        }

        log.info("JWT secret validé: {} bits", secret.length() * 8);
    }

    @Override
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiration = now.plus(jwtProperties.accessToken().expiration());

        return Jwts.builder()
                .issuer(jwtProperties.issuer())
                .audience().add(jwtProperties.audience()).and()
                .subject(user.getEmail())
                .claim(CLAIM_USER_ID, user.getId().toString())
                .claim(CLAIM_EMAIL, user.getEmail())
                .claim(CLAIM_ROLE, user.getRole().name())
                .claim(CLAIM_TYPE, TOKEN_TYPE_ACCESS)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        Instant expiration = now.plus(jwtProperties.refreshToken().expiration());

        return Jwts.builder()
                .issuer(jwtProperties.issuer())
                .audience().add(jwtProperties.audience()).and()
                .subject(user.getEmail())
                .claim(CLAIM_USER_ID, user.getId().toString())
                .claim(CLAIM_TYPE, TOKEN_TYPE_REFRESH)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.debug("Token expiré: {}", e.getMessage());
        } catch (SecurityException e) {
            log.warn("Signature JWT invalide: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Token JWT malformé: {}", e.getMessage());
        } catch (JwtException e) {
            log.warn("Token JWT invalide: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<UUID> extractUserId(String token) {
        return extractClaim(token, CLAIM_USER_ID)
                .map(UUID::fromString);
    }

    @Override
    public Optional<String> extractEmail(String token) {
        return extractClaim(token, CLAIM_EMAIL);
    }

    @Override
    public boolean isAccessToken(String token) {
        return extractClaim(token, CLAIM_TYPE)
                .map(TOKEN_TYPE_ACCESS::equals)
                .orElse(false);
    }

    @Override
    public String hashToken(String refreshToken) {
        MessageDigest digest = SHA256_DIGEST.get();
        digest.reset();
        byte[] hash = digest.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }

    /**
     * Extrait un claim spécifique du token.
     *
     * @param token     le token JWT
     * @param claimName le nom du claim à extraire
     * @return la valeur du claim ou empty si extraction impossible
     */
    private Optional<String> extractClaim(String token, String claimName) {
        try {
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            return Optional.ofNullable(claims.get(claimName, String.class));
        } catch (JwtException e) {
            log.debug("Impossible d'extraire le claim {}: {}", claimName, e.getMessage());
            return Optional.empty();
        }
    }
}