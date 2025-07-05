package com.blog.blog.service;

import com.blog.blog.dto.UserCreate;
import com.blog.blog.entities.User;
import com.blog.blog.exceptions.UserAlreadyExists;
import com.blog.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    final private UserRepository userRepository;


    /**
     * Loads user if exists from the database. It also updates the attributes, if they changed. Else creates a user and returns it.
     * @param userCreate The user to create or load from the database. Sub is used to load the user.
     * @return Retrieved or created user
     * @throws UserAlreadyExists If another user with unique attributes (besides sub) already exists.
     */
    @Transactional
    public User loadUser(@Valid UserCreate userCreate) {
        Optional<User> optionalUser = userRepository.findBySub(userCreate.sub());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (userRepository.existsUserBySubNotAndUsername(userCreate.sub(), userCreate.username())) {
                throw new UserAlreadyExists(String.format("User with different sub but same username %s already exists", userCreate.username()));
            }

            user.setUsername(userCreate.username());
            return userRepository.save(user);
        } else {
            if (userRepository.existsUserByUsername(userCreate.username())) {
                throw new UserAlreadyExists(String.format("User with different sub but same username %s already exists", userCreate.username()));
            }

            User user = User.builder()
                    .username(userCreate.username())
                    .sub(userCreate.sub())
                    .build();

            return userRepository.save(user);
        }
    }
}
