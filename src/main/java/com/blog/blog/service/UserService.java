package com.blog.blog.service;

import com.blog.blog.dto.UserLoad;
import com.blog.blog.entities.User;
import com.blog.blog.exceptions.UserAlreadyExistsException;
import com.blog.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    final private UserRepository userRepository;


    /**
     * Loads user if exists from the database. It also updates the attributes, if they changed. Else creates a user and returns it.
     * @param userLoad The user to create or load from the database. Sub is used to load the user.
     * @return Retrieved or created user
     * @throws UserAlreadyExistsException If another user with unique attributes from userLoad (besides sub) already exists.
     */
    @Transactional
    public User loadUser(@Valid UserLoad userLoad) {
        Optional<User> optionalUser = userRepository.findBySub(userLoad.sub());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (userRepository.existsUserBySubNotAndEmail(userLoad.sub(), userLoad.email())) {
                throw new UserAlreadyExistsException(String.format("User with different sub but same email %s already exists", userLoad.email()));
            }

            user.setEmail(userLoad.email());
            return userRepository.save(user);
        } else {
            if (userRepository.existsUserByEmail(userLoad.email())) {
                throw new UserAlreadyExistsException(String.format("User with different sub but same email %s already exists", userLoad.email()));
            }

            User user = User.builder()
                    .email(userLoad.email())
                    .sub(userLoad.sub())
                    .build();

            return userRepository.save(user);
        }
    }

    /**
     * Checks if a user with the sub exists in the database.
     * @param sub Sub of user to check
     * @return
     */
    @Transactional
    public boolean userExists(String sub) {
        return userRepository.existsUserBySub(sub);
    }
}
