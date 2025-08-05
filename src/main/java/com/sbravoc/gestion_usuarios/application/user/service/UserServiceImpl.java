package com.sbravoc.gestion_usuarios.application.user.service;

import com.sbravoc.gestion_usuarios.domain.user.model.User;
import com.sbravoc.gestion_usuarios.domain.user.repository.UserRepository;
import com.sbravoc.gestion_usuarios.domain.user.service.UserService;
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
    public Optional<User> getUserById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        this.userRepository.deleteById(id);
    }
}
