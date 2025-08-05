package com.sbravoc.gestion_usuarios.domain.user.service;

import com.sbravoc.gestion_usuarios.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User createUser(User user);

    void deleteUserById(Long id);
}
