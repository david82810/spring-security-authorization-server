package com.david.domain.repository;


import com.david.domain.model.OAuth2User;

public interface OAuth2UserDAO {
    OAuth2User findByProviderAndId(String provider, String id);

    OAuth2User save (OAuth2User user);
}
