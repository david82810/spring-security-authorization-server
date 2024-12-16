package com.david;

import com.david.domain.model.*;
import com.david.domain.repository.OAuth2UserDAO;
import com.david.domain.repository.RoleDAO;
import com.david.domain.repository.UserDAO;
import com.david.domain.repository.UserRoleDAO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
@Slf4j
class SpringSecurityAuthorizationServerApplicationTests {
    @Autowired
    private OAuth2UserDAO oAuth2UserDAO;

    @Test
    void test(){
        OAuth2User user = oAuth2UserDAO.findByProviderAndId(OAuth2Provider.LINE.name(), "Uf69f62ee6193ca7950a3e93623f9d0c0");
        log.info("user:{}",user);
    }

}
