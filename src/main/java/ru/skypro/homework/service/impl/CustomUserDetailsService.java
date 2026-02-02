package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.Collections;

/**
 * Custom service for loading users, extending standard logic
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Attempting to load user with email: {}", email);
        
        return userRepository.findByEmail(email)
                .map(this::createSpringUser)
                .orElseThrow(() -> {
                    log.warn("User with email {} not found", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });
    }

    /**
     * Creates UserDetails from User entity
     */
    private UserDetails createSpringUser(ru.skypro.homework.entity.User user) {

        if (!user.getEmail().contains("@")) {
            log.warn("Invalid email format for user {}", user.getEmail());
            throw new IllegalStateException("Invalid email format");
        }

        var authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
        
        log.info("User {} successfully loaded with role {}", user.getEmail(), user.getRole());
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singletonList(authority))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}