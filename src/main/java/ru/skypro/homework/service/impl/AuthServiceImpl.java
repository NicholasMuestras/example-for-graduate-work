package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.entity.User.Role;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SessionRegistry sessionRegistry;

    private String getCurrentSessionId() {
        return java.util.UUID.randomUUID().toString();
    }

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            SessionRegistry sessionRegistry
    ) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public boolean login(String userName, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, password));

            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("User {} successfully authenticated", userName);

                sessionRegistry.registerNewSession(getCurrentSessionId(), authentication.getPrincipal());

                return true;
            }
        } catch (BadCredentialsException e) {
            log.warn("Authentication failed for user {}: invalid credentials", userName);
        }

        return false;
    }

    @Override
    public boolean register(Register register) {
        String email = register.getUsername();

        if (userRepository.existsByEmail(email)) {
            log.warn("Registration failed: user with email {} already exists", email);

            return false;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setPhone(register.getPhone());
        user.setRole(Role.USER);

        userRepository.save(user);
        log.info("User {} successfully registered", email);

        return true;
    }
}
