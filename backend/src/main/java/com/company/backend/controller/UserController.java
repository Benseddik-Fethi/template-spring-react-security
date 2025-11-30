package com.company.backend.controller;

import com.company.backend.dto.request.ChangePasswordRequest;
import com.company.backend.dto.request.ForgotPasswordRequest;
import com.company.backend.dto.request.LanguageUpdateRequest;
import com.company.backend.dto.request.ResendVerificationRequest;
import com.company.backend.dto.request.ResetPasswordRequest;
import com.company.backend.dto.response.UserResponse;
import com.company.backend.security.CustomUserDetails;
import com.company.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur REST pour la gestion du compte utilisateur.
 * <p>
 * Expose les endpoints de vérification d'email, réinitialisation
 * de mot de passe et gestion du profil utilisateur.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageSource messageSource;

    /**
     * Vérifie l'adresse email avec le token reçu par email.
     *
     * @param token le token de vérification
     * @return le résultat de la vérification
     */
    @PostMapping("/verify-email")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestParam String token) {
        boolean verified = userService.verifyEmail(token);

        if (verified) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Email vérifié avec succès"
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Token invalide ou expiré"
            ));
        }
    }

    /**
     * Renvoie l'email de vérification.
     *
     * @param request l'email de l'utilisateur
     * @return un message générique (sécurité)
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<Map<String, String>> resendVerification(
            @Valid @RequestBody ResendVerificationRequest request
    ) {
        userService.resendVerificationEmail(request);

        return ResponseEntity.ok(Map.of(
                "message", "Si un compte existe avec cet email et n'est pas encore vérifié, un email a été envoyé"
        ));
    }

    /**
     * Demande une réinitialisation de mot de passe.
     *
     * @param request l'email de l'utilisateur
     * @return un message générique (sécurité)
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        userService.forgotPassword(request);

        return ResponseEntity.ok(Map.of(
                "message", "Si un compte existe avec cet email, un lien de réinitialisation a été envoyé"
        ));
    }

    /**
     * Vérifie si un token de réinitialisation est valide.
     *
     * @param token le token à vérifier
     * @return la validité du token
     */
    @GetMapping("/reset-password/validate")
    public ResponseEntity<Map<String, Boolean>> validateResetToken(@RequestParam String token) {
        boolean valid = userService.isResetTokenValid(token);
        return ResponseEntity.ok(Map.of("valid", valid));
    }

    /**
     * Réinitialise le mot de passe avec un token valide.
     *
     * @param request le token et le nouveau mot de passe
     * @return un message de confirmation
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        userService.resetPassword(request);

        return ResponseEntity.ok(Map.of(
                "message", "Mot de passe réinitialisé avec succès"
        ));
    }

    /**
     * Change le mot de passe de l'utilisateur connecté.
     *
     * @param userDetails les détails de l'utilisateur courant
     * @param request     le mot de passe actuel et le nouveau
     * @return un message de confirmation
     */
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(userDetails.getId(), request);

        return ResponseEntity.ok(Map.of(
                "message", "Mot de passe modifié avec succès"
        ));
    }

    /**
     * Récupère le profil de l'utilisateur connecté.
     *
     * @param userDetails les détails de l'utilisateur courant
     * @return les informations du profil
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserResponse user = userService.getUserById(userDetails.getId());
        return ResponseEntity.ok(user);
    }

    /**
     * Demande l'envoi d'un email de vérification (utilisateur connecté).
     *
     * @param userDetails les détails de l'utilisateur courant
     * @return un message de confirmation
     */
    @PostMapping("/send-verification")
    public ResponseEntity<Map<String, String>> sendVerificationEmail(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userService.sendVerificationEmail(userDetails.getId());

        return ResponseEntity.ok(Map.of(
                "message", "Email de vérification envoyé"
        ));
    }

    /**
     * Récupère la langue préférée de l'utilisateur connecté.
     *
     * @param userDetails les détails de l'utilisateur courant
     * @return la langue préférée
     */
    @GetMapping("/language")
    public ResponseEntity<Map<String, String>> getLanguage(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String language = userService.getLanguage(userDetails.getId());
        return ResponseEntity.ok(Map.of("language", language));
    }

    /**
     * Met à jour la langue préférée de l'utilisateur connecté.
     *
     * @param userDetails les détails de l'utilisateur courant
     * @param request     la nouvelle langue préférée
     * @return un message de confirmation
     */
    @PutMapping("/language")
    public ResponseEntity<Map<String, String>> updateLanguage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody LanguageUpdateRequest request
    ) {
        userService.updateLanguage(userDetails.getId(), request.language());

        String message = messageSource.getMessage(
                "user.language.updated",
                null,
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity.ok(Map.of(
                "message", message,
                "language", request.language()
        ));
    }
}
