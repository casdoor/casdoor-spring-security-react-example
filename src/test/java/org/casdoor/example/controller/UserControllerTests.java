package org.casdoor.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Test
    void expectUnAuthenticatedWhenApiDoesNotExist(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/404-not-found"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("unauthorized"));
    }

    @Test
    void expectUnAuthenticatedWhenNotLoggedIn(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/userinfo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("unauthorized"));
    }

    @Test
    void expectFailedToGetTokenWhenWrongCodeAndState(@Autowired MockMvc mvc) throws Exception {
        mvc
                .perform(post("/api/signin")
                        .param("code", "")
                        .param("state", "")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Cannot get OAuth token."));
    }

}