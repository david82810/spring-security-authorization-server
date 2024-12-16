package com.david.adapter.controller;

import com.david.domain.model.OAuth2User;
import com.david.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "Test";
    }

    @GetMapping("/test2")
    public String test2(Authentication authentication) {
        Object credentials = authentication.getCredentials();
        if (credentials instanceof User user) {
            log.info("User: {}", user);
        } else if (credentials instanceof OAuth2User oAuth2User) {
            log.info("OAuth2User: {}", oAuth2User);
        } else {
            log.info("Credentials: {}", credentials);
        }
        return "Test2";
    }
}
