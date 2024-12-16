package com.david.adapter.filter;

import com.david.domain.model.OAuth2User;
import com.david.domain.model.User;
import com.david.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            log.info("Token: {}", token);
            Optional<Map<String, Object>> optional = JwtUtils.validateToken(token);
            optional.ifPresent(map -> {
                log.info("Authentication: {}", map.get("sub"));
                List<String> roleList = (List<String>) map.get("role");
                List<SimpleGrantedAuthority> authorities
                        = roleList.stream().map(SimpleGrantedAuthority::new).toList();
                log.info("Authorities: {}", authorities);
                // 判斷是 User 還是 OAuth2User
                Object provider = map.get("provider");
                Authentication authentication;
                if (Objects.isNull(provider)) {
                    authentication = new UsernamePasswordAuthenticationToken(
                            map.get("sub"),
                            User.builder()
                                    .email((String)map.get("email"))
                                    .username((String)map.get("sub"))
                                    .build(), authorities
                    );
                } else {
                    authentication = new UsernamePasswordAuthenticationToken(map.get("sub"),
                            OAuth2User
                                    .builder()
                                    .id((String)map.get("sub"))
                                    .provider((String) map.get("provider"))
                                    .name((String) map.get("name"))
                                    .userId((Long) map.get("userId"))
                                    .build(), authorities);
                }

                SecurityContextHolder.getContext().setAuthentication(authentication);
            });
        }

        filterChain.doFilter(request, response);
    }
}
