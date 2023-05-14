package com.mohirdev.mohidev.service;

import com.mohirdev.mohidev.domain.User;
import com.mohirdev.mohidev.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User save(User user) {
        String password = encoder.encode(user.getPassword());
        user.setPassword(password);
        return userRepository.save(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
}
