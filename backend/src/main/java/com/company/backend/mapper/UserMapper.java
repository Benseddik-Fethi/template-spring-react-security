package com.company.backend.mapper;

import com.company.backend.domain.User;
import com.company.backend.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponse toResponse(User user);
}