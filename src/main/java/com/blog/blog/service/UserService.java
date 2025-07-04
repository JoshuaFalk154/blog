package com.blog.blog.service;

import com.blog.blog.dto.UserCreate;
import com.blog.blog.entities.User;
import com.blog.blog.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    final private UserRepository userRepository;

    public User loadUser(@Valid UserCreate userCreate) {
        return userRepository.findBySub(userCreate.sub())
                .orElse(createUser(userCreate));
    }

    public User createUser(@Valid UserCreate userCreate) {
        User user = User.builder()
                .username(userCreate.username())
                .sub(userCreate.sub())
                .build();

        return userRepository.save(user);
    }
}
