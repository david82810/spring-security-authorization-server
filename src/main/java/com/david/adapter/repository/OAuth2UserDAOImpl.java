package com.david.adapter.repository;

import com.david.adapter.repository.rowmapper.OAuth2UserRowMapper;
import com.david.domain.model.OAuth2User;
import com.david.domain.repository.OAuth2UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserDAOImpl implements OAuth2UserDAO {
    final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public OAuth2User findByProviderAndId(String provider, String id) {
        String sql = "SELECT * FROM oauth2_user WHERE provider = :provider AND id = :id";
        var oAuth2User = jdbcTemplate.query(sql, Map.of("provider", provider, "id", id), new OAuth2UserRowMapper());
        return oAuth2User.isEmpty() ? null : oAuth2User.get(0);
    }

    @Override
    public OAuth2User save(OAuth2User user) {
        String sql = "INSERT INTO oauth2_user (id,provider, user_id, name) values (:id,:provider, :user_id, :name)";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", user.getId());
        parameters.put("provider", user.getProvider());
        parameters.put("user_id", user.getUserId());
        parameters.put("name", user.getName());
        return jdbcTemplate.update(sql, new MapSqlParameterSource(parameters)) == 1 ? user : null;
    }
}
