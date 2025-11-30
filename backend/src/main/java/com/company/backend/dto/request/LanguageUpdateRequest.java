package com.company.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO pour la mise à jour de la préférence de langue de l'utilisateur.
 *
 * @param language la langue préférée (fr ou en)
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public record LanguageUpdateRequest(
        @NotBlank(message = "La langue est requise")
        @Pattern(regexp = "^(fr|en)$", message = "La langue doit être 'fr' ou 'en'")
        String language
) {
}
