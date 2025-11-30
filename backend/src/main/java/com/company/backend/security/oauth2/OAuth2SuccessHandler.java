package com.company.backend.security.oauth2;

import com.company.backend.config.JwtProperties;
import com.company.backend.domain.*;
import com.company.backend.repository.AuditLogRepository;
import com.company.backend.repository.SessionRepository;
import com.company.backend.repository.UserRepository;
import com.company.backend.security.CookieUtils;
import com.company.backend.service.JwtService;
import com.company.backend.util.IpAddressResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/**
 * Handler de succès pour l'authentification OAuth2.
 * <p>
 * Gère le flux post-authentification OAuth2 :
 * <ol>
 *   <li>Récupère les informations utilisateur du fournisseur</li>
 *   <li>Crée ou met à jour l'utilisateur en base</li>
 *   <li>Génère les tokens JWT</li>
 *   <li>Stocke les tokens en cookies HTTP-only</li>
 *   <li>Redirige vers le frontend</li>
 * </ol>
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final AuditLogRepository auditLogRepository;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final CookieUtils cookieUtils;
    private final IpAddressResolver ipAddressResolver;

    @Value("${app.security.cors.allowed-origins:http://localhost:5173}")
    private String frontendUrl;

    /**
     * Gère l'authentification OAuth2 réussie.
     *
     * @param request        la requête HTTP
     * @param response       la réponse HTTP
     * @param authentication l'authentification OAuth2
     * @throws IOException en cas d'erreur de redirection
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();
        String provider = oauthToken.getAuthorizedClientRegistrationId();

        log.debug("OAuth2 success - Provider: {}", provider);

        String email = oAuth2User.getAttribute("email");
        String providerId = oAuth2User.getAttribute("sub");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String avatar = oAuth2User.getAttribute("picture");

        if (email == null) {
            log.error("Email non fourni par le provider OAuth2");
            response.sendRedirect(frontendUrl + "/auth/error?message=email_required");
            return;
        }

        User user = findOrCreateUser(email, providerId, firstName, lastName, avatar, provider);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        createSession(user, refreshToken, request);

        cookieUtils.addAccessTokenCookie(response, accessToken);
        cookieUtils.addRefreshTokenCookie(response, refreshToken);

        auditLogRepository.save(AuditLog.oauthLogin(
                user,
                AuthProvider.valueOf(provider.toUpperCase()),
                ipAddressResolver.resolveClientIp(request),
                request.getHeader("User-Agent")
        ));

        log.info("OAuth2 login réussi pour: {} - Tokens en cookies HTTP-only", email);

        getRedirectStrategy().sendRedirect(request, response, frontendUrl + "/auth/callback");
    }

    /**
     * Recherche ou crée un utilisateur à partir des données OAuth2.
     *
     * @param email      l'adresse email
     * @param providerId l'identifiant chez le fournisseur
     * @param firstName  le prénom
     * @param lastName   le nom de famille
     * @param avatar     l'URL de l'avatar
     * @param provider   le nom du fournisseur
     * @return l'utilisateur trouvé ou créé
     */
    private User findOrCreateUser(
            String email,
            String providerId,
            String firstName,
            String lastName,
            String avatar,
            String provider
    ) {
        return userRepository.findByGoogleId(providerId)
                .orElseGet(() -> userRepository.findByEmail(email)
                        .map(existingUser -> {
                            existingUser.setGoogleId(providerId);
                            if (existingUser.getAvatar() == null) {
                                existingUser.setAvatar(avatar);
                            }
                            return userRepository.save(existingUser);
                        })
                        .orElseGet(() -> {
                            User newUser = User.builder()
                                    .email(email)
                                    .firstName(firstName)
                                    .lastName(lastName)
                                    .avatar(avatar)
                                    .googleId(providerId)
                                    .provider(AuthProvider.GOOGLE)
                                    .role(Role.USER)
                                    .emailVerified(true)
                                    .build();

                            log.info("Nouvel utilisateur créé via OAuth2: {}", email);
                            return userRepository.save(newUser);
                        }));
    }

    /**
     * Crée une session pour l'utilisateur authentifié.
     *
     * @param user         l'utilisateur
     * @param refreshToken le refresh token
     * @param request      la requête HTTP
     */
    private void createSession(User user, String refreshToken, HttpServletRequest request) {
        Session session = Session.builder()
                .user(user)
                .refreshTokenHash(jwtService.hashToken(refreshToken))
                .ipAddress(ipAddressResolver.resolveClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .expiresAt(Instant.now().plus(jwtProperties.refreshToken().expiration()))
                .build();

        sessionRepository.save(session);
    }
}