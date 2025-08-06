package com.sbravoc.gestion_usuarios.adapter.rest;

import com.sbravoc.gestion_usuarios.adapter.rest.dto.CreateUserRequestDto;
import com.sbravoc.gestion_usuarios.adapter.rest.dto.UpdateUserRequestDto;
import com.sbravoc.gestion_usuarios.adapter.rest.dto.UserResponseDto;
import com.sbravoc.gestion_usuarios.adapter.rest.mapper.UserMapper;
import com.sbravoc.gestion_usuarios.domain.user.model.User;
import com.sbravoc.gestion_usuarios.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = this.userService.getAllUsers();
        List<UserResponseDto> userDtos = this.userMapper.toDtoList(users);
        return ResponseEntity.status(HttpStatus.OK).body(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        try {
            User user = this.userService.getUserById(id);
            UserResponseDto userDto = this.userMapper.toDto(user);
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        User user = this.userMapper.toEntityFromCreateRequest(createUserRequestDto);
        User createdUser = this.userService.createUser(user);
        UserResponseDto userResponseDto = this.userMapper.toDto(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        try {
            User userToUpdate = this.userMapper.toEntityFromUpdateRequest(updateUserRequestDto);
            User updatedUser = this.userService.updateUser(id, userToUpdate);
            UserResponseDto userResponseDto = this.userMapper.toDto(updatedUser);
            return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        try {
            this.userService.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
