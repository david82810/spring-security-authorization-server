package com.david.configuration;

import com.david.adapter.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public RestTemplate restTemplate (){
        return new RestTemplate();
    }


    @Bean
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 無狀態
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/member/login", "/oauth2/**").permitAll()  // 允許訪問登錄 API 和 OAuth2 登錄端點
                                .requestMatchers(HttpMethod.GET, "/test").hasRole("ADMIN")  // 需要 admin 權限的請求
                                .requestMatchers(HttpMethod.GET,"/test2").hasAnyRole("ADMIN","USER")
                                .anyRequest().authenticated()  // 其他所有請求需要認證
                )
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);  // JWT 認證過濾器應該先執行

        return http.build();
    }

    @Bean
    public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // OAuth2 需要使用 session
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/member/login", "/oauth2/**").permitAll()  // 允許訪問登錄 API 和 OAuth2 登錄端點
                                .anyRequest().authenticated()  // 其他所有請求需要認證
                )
                .formLogin(AbstractHttpConfigurer::disable)  // 禁用表單登錄
                .oauth2Login(oauth2Login ->  // 配置 OAuth2 登錄
                        oauth2Login
                                .defaultSuccessUrl("/loginSuccess")  // OAuth2 登錄成功後的重定向
                                .failureUrl("/loginFailure")  // 登錄失敗時的重定向
                );

        return http.build();
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration lineClientRegistration = ClientRegistration.withRegistrationId("line")
                .clientId("2006654139")
                .clientSecret("b8d1dfd9a4829efcc1cb2daabdf3e9c3")
                .scope("profile", "openid", "email")
                .authorizationUri("https://access.line.me/oauth2/v2.1/authorize")
                .tokenUri("https://api.line.me/oauth2/v2.1/token")
                .userInfoUri("https://api.line.me/v2/profile")
                .userNameAttributeName("userId")
                .clientName("Sign in with LINE")
                .redirectUri("http://localhost:8888/oauth2/line/callback")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();

        return new InMemoryClientRegistrationRepository(lineClientRegistration);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //1.允許任何來源
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        //2.允許任何請求頭
        configuration.addAllowedHeader(CorsConfiguration.ALL);
        //3.允許任何方法
        configuration.addAllowedMethod(CorsConfiguration.ALL);
        //4.允許憑證
        configuration.setAllowCredentials(true);
        //5.允許對外曝光的請求頭
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}

