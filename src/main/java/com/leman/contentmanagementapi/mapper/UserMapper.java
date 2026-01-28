package com.leman.contentmanagementapi.mapper;

import com.leman.contentmanagementapi.dto.request.RegisterRequest;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
        componentModel = SPRING,
        unmappedSourcePolicy = IGNORE,
        unmappedTargetPolicy = IGNORE,
        injectionStrategy = CONSTRUCTOR
)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    User toEntity(RegisterRequest request);

    UserResponse toResponse(User user);

}
