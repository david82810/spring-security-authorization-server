package com.david.adapter.service;

import com.david.adapter.controller.response.LoginResponse;
import com.david.adapter.controller.response.oauth2.line.LineProfileResponse;
import com.david.adapter.controller.response.oauth2.line.LineTokenResponse;
import com.david.domain.model.OAuth2Provider;
import com.david.domain.model.OAuth2User;
import com.david.domain.model.Role;
import com.david.domain.model.User;
import com.david.domain.repository.OAuth2UserDAO;
import com.david.domain.repository.UserDAO;
import com.david.exception.CustomerException;
import com.david.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class LineOAuth2Service {
    final RestTemplate restTemplate;

    final OAuth2UserDAO oAuth2UserDAO;

    final UserDAO userDAO;
    @Value("${spring.security.oauth2.client.registration.line.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.line.client-secret}")
    private String secret;
    @Value("${spring.security.oauth2.client.registration.line.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.provider.line.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.provider.line.user-info-uri}")
    private String userInfoUri;

    @Transactional
    public LoginResponse lineLogin(String code) {
        // STEP1 取得 Access Token
        LineTokenResponse tokenResponse = this.getToken(code);
        // STEP2 call Profile check Token valid
        LineProfileResponse userInfo = this.getUserProfile(tokenResponse);
        // STEP3 save user info (query by providerID and id) 沒有直接寫入一筆,有得話查看是否有綁定當前應用使用者
      return this.getOrSaveUserInfo(userInfo);
    }

    private LoginResponse getOrSaveUserInfo(LineProfileResponse userInfo) {
        String userId = userInfo.getUserId();
        String displayName = userInfo.getDisplayName();
        OAuth2User user = oAuth2UserDAO.findByProviderAndId(OAuth2Provider.LINE.name(), userId);
        // OAuth2User 尚未紀錄,寫入一筆
        String token;
        if (Objects.isNull(user)) {
            OAuth2User oauth2User = new OAuth2User();
            oauth2User.setProvider(OAuth2Provider.LINE.name());
            oauth2User.setId(userId);
            oauth2User.setUserId(null);
            oauth2User.setName(displayName);
            OAuth2User save = oAuth2UserDAO.save(oauth2User);
            token = JwtUtils.generateToken(save);
        } else {
            // 查詢當前系統使用者是否存在,若存在改用當前系統使用者組 JWT Token;不存在將 Oauth2User 組 JWT Token
            Long appUserId = user.getUserId();

            if (Objects.isNull(appUserId)) {
                token = JwtUtils.generateToken(user);
            } else {
                User appUser = userDAO.findById(appUserId);
                if (Objects.isNull(appUser)) {
                    token = JwtUtils.generateToken(user);
                } else {
                    List<Role> roleList = userDAO.findUserRoles(appUserId);
                    token = JwtUtils.generateToken(appUser, roleList);
                }
            }

        }
        return LoginResponse.builder().token(token).build();
    }


    private LineProfileResponse getUserProfile(LineTokenResponse resp) {
        String accessToken = resp.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<LineProfileResponse> exchange = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, LineProfileResponse.class);
            return exchange.getBody();
        } catch (Exception e) {
            log.error("getUserProfile error:{}", e.getMessage());
            throw new CustomerException(e.getLocalizedMessage());
        }
    }

    /**
     * 取得 Access Token
     *
     * @param code
     * @return
     */
    private LineTokenResponse getToken(String code) {
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "authorization_code");
        body.put("code", code);
        body.put("client_id", clientId);
        body.put("client_secret", secret);
        body.put("redirect_uri", redirectUri);
        log.info("params:{}", body);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        body.forEach((key, value) -> params.add(key, value));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        try {
            // 取得 Access Token
            ResponseEntity<LineTokenResponse> exchange = restTemplate.exchange(tokenUri, HttpMethod.POST, requestEntity, LineTokenResponse.class);
            return exchange.getBody();
        } catch (Exception e) {
            log.error("getLoginToken error:{}", e.getMessage());
            throw new CustomerException(e.getLocalizedMessage());
        }
    }
}
