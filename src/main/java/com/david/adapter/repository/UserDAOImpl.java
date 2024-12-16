package com.david.adapter.repository;

import com.david.adapter.repository.rowmapper.RoleRowMapper;
import com.david.adapter.repository.rowmapper.UserRowMapper;
import com.david.domain.model.Role;
import com.david.domain.model.User;
import com.david.domain.repository.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class UserDAOImpl implements UserDAO {
    final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        String sql = "insert into user (username,password,email) values (:username,:password,:email)";
        Map<String, Object> map = Map.of("username", user.getUsername(), "password", user.getPassword(), "email", user.getEmail());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(map);
        int update = jdbcTemplate.update(sql, params, keyHolder);
        if (update == 1) {
            user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        }
        return user;
    }

    @Override
    public User findById(Long id) {
        String sql = "select id,password,username,email from user where id = :id";
        Map<String, Object> map = Map.of("id", id);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(map);
        List<User> result = jdbcTemplate.query(sql, params, new UserRowMapper());
        return CollectionUtils.isEmpty(result) ? null : result.get(0);
    }

    @Override
    public User findByUsername(String username) {
        String sql = "select id,password,username,email from user where username = :username";
        Map<String, Object> map = Map.of("username", username);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(map);
        List<User> result = jdbcTemplate.query(sql, params, new UserRowMapper());
        return CollectionUtils.isEmpty(result) ? null : result.get(0);
    }

    @Override
    public User findByEmail(String email) {
        String sql = "select id,password,username,email from user where email = :email";
        Map<String, Object> map = Map.of("email", email);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(map);
        List<User> result = jdbcTemplate.query(sql, params, new UserRowMapper());
        return CollectionUtils.isEmpty(result) ? null : result.get(0);
    }

    @Override
    public List<Role> findUserRoles(Long userId) {
        String sql = "select role.name,role.id from role\n" +
                "join user_roles on role.id = user_roles.role_id\n" +
                "join user on user_roles.user_id = user.id where user.id = :userId";
        Map<String, Object> map = Map.of("userId", userId);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(map);
        return jdbcTemplate.query(sql, params, new RoleRowMapper());
    }
}
