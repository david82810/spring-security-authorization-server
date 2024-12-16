package com.david.adapter.service;

import com.david.adapter.controller.request.LoginRequest;
import com.david.adapter.controller.response.LoginResponse;
import com.david.domain.model.Role;
import com.david.domain.model.User;
import com.david.domain.repository.UserDAO;
import com.david.exception.CustomerException;
import com.david.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserDAO userDAO;
    final PasswordEncoder passwordEncoder;


    public LoginResponse login(LoginRequest request) {
        User user;
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (request.getUsername().matches(regex)) {
            user = userDAO.findByEmail(request.getUsername());
        } else {
            user = userDAO.findByUsername(request.getUsername());
        }

        if (Objects.isNull(user)) {
            throw new CustomerException("帳號或密碼錯誤");
        }
        String password = user.getPassword();
        if (!passwordEncoder.matches(request.getPassword(), password)) {
            throw new CustomerException("帳號或密碼錯誤");
        }
        List<Role> roleList = userDAO.findUserRoles(user.getId());
        return LoginResponse.builder()
                .account(user.getUsername())
                .token(JwtUtils.generateToken(user, roleList))
                .build();

    }

}
