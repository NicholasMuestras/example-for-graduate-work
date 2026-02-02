package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UsersService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UsersServiceImpl implements UsersService {

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Value("${path.to.image.folder:src/main/resources/images}")
    private String imageDir;

    public UsersServiceImpl(
            UserRepository userRepository,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public io.swagger.model.User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        return userMapper.toDto(user);
    }

    @Override
    public boolean setPassword(io.swagger.model.NewPassword newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public io.swagger.model.UpdateUser updateUser(io.swagger.model.UpdateUser updateUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());

        userRepository.save(user);

        io.swagger.model.UpdateUser result = new io.swagger.model.UpdateUser();
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setPhone(user.getPhone());

        return result;
    }

    @Override
    public boolean updateUserImage(byte[] image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        try {
            Path imageDirPath = Paths.get(imageDir);
            if (!Files.exists(imageDirPath)) {
                Files.createDirectories(imageDirPath);
            }

            String fileName = email.replaceAll("[^a-zA-Z0-9]", "_") + "_avatar";
            Path imagePath = imageDirPath.resolve(fileName);

            Files.write(imagePath, image);

            user.setImage("/" + fileName);
            userRepository.save(user);

            return true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save user image", e);
        }
    }
}
