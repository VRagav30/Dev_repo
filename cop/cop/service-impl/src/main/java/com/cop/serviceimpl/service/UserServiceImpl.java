package com.cop.serviceimpl.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cop.model.security.User;
import com.cop.repository.transaction.UserRepository;
import com.cop.serviceapi.service.UserService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(User user, String roleName) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.register(user, roleName);
    }

    @PreAuthorize("hasAuthority('manageUser')")
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
