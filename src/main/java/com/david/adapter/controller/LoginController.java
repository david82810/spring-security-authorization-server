package com.david.adapter.controller;

import com.david.adapter.controller.request.LoginRequest;
import com.david.adapter.controller.response.LoginResponse;
import com.david.adapter.controller.response.Result;
import com.david.adapter.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    final UserService userService;

    @PostMapping(value = "/member/login")
    public Result<LoginResponse> login(@RequestBody @Validated LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResp = userService.login(loginRequest);
        response.setHeader("Authorization",loginResp.getToken());
        return Result.success(loginResp);
    }

}
