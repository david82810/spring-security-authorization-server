package com.david.adapter.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {

    @NotEmpty(message = "請輸入帳號")
    private String username;
    @NotEmpty(message = "請輸入密碼")
    private String password;
}
