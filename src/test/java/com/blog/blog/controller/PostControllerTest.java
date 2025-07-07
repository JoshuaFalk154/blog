package com.blog.blog.controller;

import com.blog.blog.controllers.PostController;
import com.blog.blog.dto.PostCreate;
import com.blog.blog.entities.User;
import com.blog.blog.security.JwtConverter;
import com.blog.blog.security.MyAuthenticationToken;
import com.blog.blog.security.SecurityConfig;
import com.blog.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostController.class)
public class PostControllerTest {


    @Autowired
    MockMvcTester mockMvc;

    @MockitoBean
    PostService postService;

    @MockitoBean
    JwtConverter jwtConverter;


//    @Test
//    void test() {
//        mockMvc.post()
//                .uri("/posts")
//                .exchange()
//                .assertThat()
//                .hasStatus(HttpStatus.OK);
//    }

//    @BeforeEach
//    void before() {
//        Authentication authentication = Mockito.mock(Authentication.class);
//        // Mockito.whens() for your authorization object
//        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//    }

//    @Test
//    void oauth2LoginTest() throws Exception {
//        String requestBody = """
//                {
//                    "title": "sometiel",
//                    "body": "some body"
//                }
//                """;
//
////        //String requestBody = new ObjectMapper().writeValueAsString(postCreate);
////        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
////                        .content(requestBody))
////                .andExpect(status().isOk());
//
//
//
//        mockMvc.post()
//                .uri("/posts")
//                .with(SecurityMockMvcRequestPostProcessors.authentication(new MyAuthenticationToken(null, User.builder().sub("somesub").build(), null)))
//                .content(requestBody)
//                .contentType(MediaType.APPLICATION_JSON);
//    }
}
