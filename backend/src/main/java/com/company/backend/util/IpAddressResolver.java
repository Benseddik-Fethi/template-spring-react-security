package com.company.backend.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Résolveur sécurisé d'adresse IP client avec protection anti-spoofing.
 * <p>
 * Empêche les attaquants de forger l'IP via X-Forwarded-For en ne faisant
 * confiance à ce header que si la requête provient d'un proxy autorisé.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Component
@Slf4j
public class IpAddressResolver {

    private final Set<String> trustedProxies;

    /**
     * Constructeur avec configuration des proxies de confiance.
     *
     * @param trustedProxiesConfig liste des IPs de proxies de confiance (séparées par des virgules)
     */
    public IpAddressResolver(
            @Value("${app.security.trusted-proxies:127.0.0.1,::1}") String trustedProxiesConfig
    ) {
        this.trustedProxies = new HashSet<>(Arrays.asList(trustedProxiesConfig.split(",")));
        log.info("Trusted proxies configurés : {}", trustedProxies);
    }

    /**
     * Extrait l'IP réelle du client avec protection anti-spoofing.
     * <p>
     * Logique de résolution :
     * <ol>
     *   <li>Si la requête ne vient pas d'un proxy de confiance, retourne l'IP directe</li>
     *   <li>Si la requête vient d'un proxy de confiance, utilise X-Forwarded-For</li>
     *   <li>Fallback sur X-Real-IP si X-Forwarded-For n'est pas présent</li>
     * </ol>
     * </p>
     *
     * @param request la requête HTTP
     * @return l'adresse IP réelle du client
     */
    public String resolveClientIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();

        if (!trustedProxies.contains(remoteAddr)) {
            log.trace("IP directe (pas de proxy de confiance) : {}", remoteAddr);
            return remoteAddr;
        }

        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(forwardedFor)) {
            String clientIp = forwardedFor.split(",")[0].trim();
            log.trace("IP résolue via X-Forwarded-For : {} (remoteAddr={})", clientIp, remoteAddr);
            return clientIp;
        }

        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isEmpty() && !"unknown".equalsIgnoreCase(realIp)) {
            log.trace("IP résolue via X-Real-IP : {} (remoteAddr={})", realIp, remoteAddr);
            return realIp;
        }

        log.trace("Aucun header valide, IP du proxy : {}", remoteAddr);
        return remoteAddr;
    }
}
