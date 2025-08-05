package com.sbravoc.gestion_usuarios.adapter.rest.mapper;

import com.sbravoc.gestion_usuarios.adapter.rest.dto.UserResponseDto;
import com.sbravoc.gestion_usuarios.domain.user.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    // Convertir un objeto User a UserResponseDto
    public UserResponseDto toDto(User user) {
        if (user == null) return null;

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        return dto;
    }

    // Convertir lista de Users a lista de UserResponseDto
    public List<UserResponseDto> toDtoList(List<User> users) {
        if (users == null) return null;

        return users.stream()
                .map(user -> this.toDto(user))
                .collect(Collectors.toList());
    }

    // Convertir UserResponseDto a una entidad User
    public User toEntity(UserResponseDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }
}
