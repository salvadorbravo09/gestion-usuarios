package com.sbravoc.gestion_usuarios.domain.user.service;

import com.sbravoc.gestion_usuarios.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    Page<User> findAllUsers(Pageable pageable);

    User getUserById(Long id);

    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUserById(Long id);
}
