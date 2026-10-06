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

import org.casbin.casdoor.exception.AuthException;
import org.casbin.casdoor.service.AuthService;
import org.casdoor.example.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Exchanges the code from Casdoor for an access token. The frontend checks the state before calling this.
     */
    @PostMapping("/api/signin")
    public ResponseEntity<Result> signin(@RequestParam String code, @RequestParam String state) {
        try {
            return ResponseEntity.ok(Result.success(authService.getOAuthToken(code, state)));
        } catch (AuthException exception) {
            logger.error("failed to get the access token from Casdoor", exception);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.failure(exception.getMessage()));
        }
    }

    /**
     * Returns the signed-in user, read from the claims of the verified access token.
     */
    @GetMapping("/api/userinfo")
    public Result userinfo(@AuthenticationPrincipal Jwt jwt) {
        return Result.success(jwt.getClaims());
    }

    /**
     * Ends the Casdoor session of the access token.
     */
    @PostMapping("/api/logout")
    public Result logout(@AuthenticationPrincipal Jwt jwt) {
        try {
            authService.logoutCurrentSession(jwt.getTokenValue());
        } catch (RuntimeException exception) {
            // the session may already have ended
            logger.warn("failed to end the Casdoor session", exception);
        }
        return Result.success(null);
    }
}
