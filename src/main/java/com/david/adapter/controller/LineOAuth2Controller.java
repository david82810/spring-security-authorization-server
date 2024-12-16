package com.david.adapter.controller;

import com.david.adapter.controller.response.LoginResponse;
import com.david.adapter.service.LineOAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/oauth2/line")
@Slf4j
@RequiredArgsConstructor
public class LineOAuth2Controller {

    @Value("${spring.security.oauth2.client.registration.line.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.line.client-secret}")
    private String secret;
    @Value("${spring.security.oauth2.client.registration.line.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.line.scope}")
    private String scope;
    @Value("${spring.security.oauth2.client.provider.line.authorization-uri}")
    private String authorizationUri;

    final LineOAuth2Service service;


    /**
     * 跳轉到 Line OAuth2 登入頁面
     * @return
     */
    @GetMapping("")
    public RedirectView lineLogin(){
        String state = UUID.randomUUID().toString();
        String path = authorizationUri
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&state=" + state
                + "&scope=" + scope;
        return new RedirectView(path);

    }


    /**
     * 處理 Line OAuth2 登入後的 callback
     * 拿 code 去取 token 並取得使用者資訊,組成系統 JWT Token 回給前端
     * @param request
     * @return
     */
    @GetMapping(value = "/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        LoginResponse loginResponse = service.lineLogin(code);
        response.sendRedirect("http://localhost:3000/oauth2Page?token=" + loginResponse.getToken());
    }
}
