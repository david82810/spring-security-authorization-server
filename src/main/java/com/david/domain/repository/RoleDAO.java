package com.david.domain.repository;

import com.david.domain.model.Role;

import java.util.List;

public interface RoleDAO {
    List<Role> findAllRoles();
}
