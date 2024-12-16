package com.david.domain.repository;

import com.david.domain.model.Role;
import com.david.domain.model.User;

import java.util.List;

public interface UserDAO {
    User findById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    List<Role> findUserRoles(Long userId);
    User addUser(User user);
}
