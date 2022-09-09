// Copyright 2022 The Casdoor Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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