package com.sbravoc.gestion_usuarios.adapter.rest;

import com.sbravoc.gestion_usuarios.adapter.rest.dto.CreateUserRequestDto;
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
@RequestMapping("/api/users")
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
        Optional<User> userOptional = this.userService.getUserById(id);
        if (userOptional.isPresent()) {
            UserResponseDto userDto = this.userMapper.toDto(userOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        User user = this.userMapper.toEntityFromCreateRequest(createUserRequestDto);
        User createdUser = this.userService.createUser(user);
        UserResponseDto userResponseDto = this.userMapper.toDto(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }
}
