package com.david.adapter.repository;

import com.david.adapter.repository.rowmapper.RoleRowMapper;
import com.david.domain.model.Role;
import com.david.domain.repository.RoleDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class RoleDAOImpl implements RoleDAO {
    final NamedParameterJdbcTemplate jdbcTemplate;
    @Override
    public List<Role> findAllRoles() {
        String sql = "select id,name from role";
        return jdbcTemplate.query(sql, Map.of(), new RoleRowMapper());
    }
}
