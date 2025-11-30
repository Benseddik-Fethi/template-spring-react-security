package com.company.backend.security;

import com.company.backend.config.SecurityProperties;
import com.company.backend.util.IpAddressResolver;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Filtre de rate limiting par adresse IP.
 * <p>
 * Protège l'application contre les attaques DDoS au niveau applicatif,
 * le brute force distribué et l'abus d'API. Utilise l'algorithme Token Bucket
 * avec cache Caffeine pour le stockage en mémoire.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;
    private final IpAddressResolver ipAddressResolver;

    private final Cache<String, Bucket> cache = Caffeine.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .maximumSize(100_000)
            .build();

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (!securityProperties.rateLimit().enabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = ipAddressResolver.resolveClientIp(request);
        String path = request.getServletPath();

        boolean isAuth = isAuthEndpoint(path);
        int limit = isAuth
                ? securityProperties.rateLimit().authRequestsPerMinute()
                : securityProperties.rateLimit().requestsPerMinute();

        String bucketKey = ip + (isAuth ? ":auth" : ":api");

        Bucket bucket = cache.get(bucketKey, key -> createBucket(limit));

        if (bucket == null || !bucket.tryConsume(1)) {
            log.warn("Rate limit dépassé pour IP: {} sur {}", ip, path);
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write(
                    String.format(
                            "{\"error\":\"Too Many Requests\",\"message\":\"Limite de %d requêtes/minute dépassée. Réessayez plus tard.\"}",
                            limit
                    )
            );
            return;
        }

        long remaining = bucket.getAvailableTokens();
        response.setHeader("X-RateLimit-Limit", String.valueOf(limit));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(remaining));

        filterChain.doFilter(request, response);
    }

    /**
     * Crée un bucket avec la limite spécifiée (Token Bucket Algorithm).
     *
     * @param requestsPerMinute le nombre de requêtes autorisées par minute
     * @return le bucket configuré
     */
    private Bucket createBucket(int requestsPerMinute) {
        Bandwidth limit = Bandwidth.classic(
                requestsPerMinute,
                Refill.intervally(requestsPerMinute, Duration.ofMinutes(1))
        );
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * Détermine si l'endpoint est un endpoint d'authentification.
     *
     * @param path le chemin de la requête
     * @return {@code true} si c'est un endpoint d'authentification
     */
    private boolean isAuthEndpoint(String path) {
        return path.startsWith("/api/v1/auth/login") ||
                path.startsWith("/api/v1/auth/register") ||
                path.startsWith("/api/v1/auth/refresh") ||
                path.startsWith("/api/v1/users/forgot-password") ||
                path.startsWith("/api/v1/users/reset-password");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/actuator/health");
    }
}
