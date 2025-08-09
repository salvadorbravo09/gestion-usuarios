package com.sbravoc.gestion_usuarios.adapter.rest;

import com.sbravoc.gestion_usuarios.adapter.rest.dto.CreateUserRequestDto;
import com.sbravoc.gestion_usuarios.adapter.rest.dto.UpdateUserRequestDto;
import com.sbravoc.gestion_usuarios.adapter.rest.dto.UserResponseDto;
import com.sbravoc.gestion_usuarios.adapter.rest.mapper.UserMapper;
import com.sbravoc.gestion_usuarios.domain.user.model.User;
import com.sbravoc.gestion_usuarios.domain.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/page/{page}")
    public ResponseEntity<Page<UserResponseDto>> getAllUsersPaginated(@PathVariable int page) {
        Pageable pageable = PageRequest.of(page, 5); // Cada pagina contiene 5 registros
        Page<User> userPage = this.userService.findAllUsers(pageable);
        Page<UserResponseDto> userDtoPage = userPage.map(user -> this.userMapper.toDto(user));
        return ResponseEntity.status(HttpStatus.OK).body(userDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        User user = this.userService.getUserById(id);
        UserResponseDto userDto = this.userMapper.toDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        User user = this.userMapper.toEntityFromCreateRequest(createUserRequestDto);
        User createdUser = this.userService.createUser(user);
        UserResponseDto userResponseDto = this.userMapper.toDto(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        User userToUpdate = this.userMapper.toEntityFromUpdateRequest(updateUserRequestDto);
        User updatedUser = this.userService.updateUser(id, userToUpdate);
        UserResponseDto userResponseDto = this.userMapper.toDto(updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        this.userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
