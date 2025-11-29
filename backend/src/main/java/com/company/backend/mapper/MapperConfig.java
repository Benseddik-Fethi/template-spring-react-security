package com.company.backend.mapper;

import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Configuration globale pour MapStruct.
 * - Intégration Spring (Injection de dépendances)
 * - Ignorer les champs non mappés (pour éviter les warnings inutiles)
 */
@org.mapstruct.MapperConfig(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MapperConfig {
}