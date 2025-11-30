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
        @NotBlank(message = "{validation.language.required}")
        @Pattern(regexp = "^(fr|en)$", message = "{validation.language.invalid}")
        String language
) {
}
