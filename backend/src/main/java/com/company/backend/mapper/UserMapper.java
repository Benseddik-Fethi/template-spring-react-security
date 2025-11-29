package com.company.templatespringreactsecurity.mapper;

import com.company.templatespringreactsecurity.domain.User;
import com.company.templatespringreactsecurity.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponse toResponse(User user);
}