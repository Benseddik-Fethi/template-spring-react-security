package com.company.backend.mapper;

import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Configuration globale pour MapStruct.
 * <p>
 * Définit les paramètres communs à tous les mappers :
 * intégration Spring pour l'injection de dépendances et
 * politique d'ignorance des champs non mappés.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@org.mapstruct.MapperConfig(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MapperConfig {
}