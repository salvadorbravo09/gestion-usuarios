package com.sbravoc.gestion_usuarios.application.user.service;

import com.sbravoc.gestion_usuarios.domain.user.model.User;
import com.sbravoc.gestion_usuarios.domain.user.repository.UserRepository;
import com.sbravoc.gestion_usuarios.domain.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return (List<User>) this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAllUsers(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("Usuario con id " + id + " no existe.");
        }
        return userOptional.get();
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        Optional<User> existingUser = this.userRepository.findById(id);
        if (!existingUser.isPresent()) {
            throw new IllegalArgumentException("Usuario con id " + id + " no existe.");
        }
        user.setId(id);
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("Usuario con id " + id + " no existe.");
        }
        this.userRepository.deleteById(id);
    }
}
