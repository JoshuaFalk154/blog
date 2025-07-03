package com.blog.blog.service;

import com.blog.blog.entities.User;
import com.blog.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    final private UserRepository userRepository;

    public User loadUser(String sub) {
        Optional<User> optionalUser = userRepository.findBySub(sub);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = User.builder()
                .email("email")
                .sub(sub)
                .build();
        userRepository.save(user);

        return user;
    }

}
