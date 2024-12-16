package com.david.adapter.repository.rowmapper;

import com.david.domain.model.OAuth2User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OAuth2UserRowMapper implements RowMapper<OAuth2User> {
    @Override
    public OAuth2User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return OAuth2User
                .builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .provider(rs.getString("provider"))
                .userId(rs.getLong("user_id") == 0 ? null : rs.getLong("user_id"))
                .build();
    }
}
