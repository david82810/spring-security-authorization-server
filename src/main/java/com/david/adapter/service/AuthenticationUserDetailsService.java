//package com.david.adapter.service;
//
//import com.david.domain.model.Role;
//import com.david.domain.model.User;
//import com.david.domain.repository.UserDAO;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Objects;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class AuthenticationUserDetailsService implements UserDetailsService {
//    final UserDAO userDAO;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.info("loadUserByUsername: {}", username);
//        User user = userDAO.findByUsername(username);
//        if (Objects.isNull(user)) {
//            throw new UsernameNotFoundException("User not found, username = " + username);
//        }
//        Long id = user.getId();
//        List<Role> roles = userDAO.findUserRoles(id);
//        List<GrantedAuthority> authorities = roles.stream()
//                .map(role -> (GrantedAuthority) role::getName)
//                .toList();
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
//    }
//}
