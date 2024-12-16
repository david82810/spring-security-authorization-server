package com.david.adapter.repository;

import com.david.domain.model.UserRole;
import com.david.domain.repository.UserRoleDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserRoleDAOImpl implements UserRoleDAO {
    final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public UserRole addUserRole(UserRole userRole) {
        String sql = "insert into user_roles (user_id,role_id) values (:userId,:roleId)";
        Map<String, Object> map = Map.of("userId", userRole.getUserId(), "roleId", userRole.getRoleId());
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(map);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcTemplate.update(sql, params, keyHolder);
        if (update == 1) {
            userRole.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
            return userRole;
        }
        return null;
    }
}
