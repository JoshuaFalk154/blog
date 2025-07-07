package com.blog.blog.controller;

import com.blog.blog.MyTestcontainersConfiguration;
import com.blog.blog.TestBlogApplication;

import com.blog.blog.controllers.PostController;
import com.blog.blog.dto.PostCreate;
import com.blog.blog.dto.UserLoad;
import com.blog.blog.entities.User;
import com.blog.blog.security.MyAuthenticationToken;
import com.blog.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.assertj.MockMvcTester;


import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MyTestcontainersConfiguration.class)
@AutoConfigureMockMvc
public class PostControllerIT {

    @Autowired
    MockMvcTester mvc;

    @Autowired
    UserService userService;

    User user;

    @BeforeEach
    void before() {
        user = userService.loadUser(new UserLoad("username", "somesub"));
    }


    @Test
    void PostController_addPost_CREATED() throws JsonProcessingException {
        String requestBody = new ObjectMapper().writeValueAsString(new PostCreate("some title", "some body"));

        mvc.post()
                .uri("/posts")
                .with(SecurityMockMvcRequestPostProcessors.authentication(new MyAuthenticationToken(null, user, null)))
                .with(csrf())
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.CREATED);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPostCreate")
    void PostController_addPost_InvalidPostCreate_BADREQUEST(PostCreate postCreate) throws JsonProcessingException {
        String requestBody = new ObjectMapper().writeValueAsString(postCreate);

        mvc.post()
                .uri("/posts")
                .with(SecurityMockMvcRequestPostProcessors.authentication(new MyAuthenticationToken(null, user, null)))
                .with(csrf())
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

    static Stream<Arguments> provideInvalidPostCreate() {
        return Stream.of(
                Arguments.of(new PostCreate("", "some bodyyyyy")),
                Arguments.of(new PostCreate("title", "")),
                Arguments.of(new PostCreate("", "")),
                Arguments.of(new PostCreate("  ", "some bodyyyyy")),
                Arguments.of(new PostCreate("title", "     ")),
                Arguments.of(new PostCreate("  ", "  "))
        );
    }
}
