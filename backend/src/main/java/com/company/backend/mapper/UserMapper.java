package com.company.backend.mapper;

import com.company.backend.domain.User;
import com.company.backend.dto.response.UserResponse;
import org.mapstruct.Mapper;

/**
 * Mapper pour la conversion entre l'entité User et ses DTOs.
 * <p>
 * Utilise MapStruct pour générer automatiquement les méthodes
 * de mapping à la compilation.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Mapper(config = MapperConfig.class)
public interface UserMapper {

    /**
     * Convertit une entité User en UserResponse.
     *
     * @param user l'entité utilisateur
     * @return le DTO de réponse
     */
    UserResponse toResponse(User user);
}