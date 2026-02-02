package ru.skypro.homework.controller;

import io.swagger.model.NewPassword;
import io.swagger.model.UpdateUser;
import io.swagger.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.UsersService;

import java.io.IOException;

/**
 * REST controller for user management
 * Provides endpoints for getting, updating user information and changing password
 */
@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * Get information about authenticated user
     * @return User information
     */
    @GetMapping("/me")
    public ResponseEntity<User> getUser() {
        User user = usersService.getUser();
        return ResponseEntity.ok(user);
    }

    /**
     * Update password for authenticated user
     * @param newPassword New password data
     * @return ResponseEntity with status
     */

    @PatchMapping("/set_password")
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPassword newPassword) {
        boolean result = usersService.setPassword(newPassword);
        return result ? ResponseEntity.ok().build() : ResponseEntity.status(403).build();
    }

    /**
     * Update information about authenticated user
     * @param updateUser User data to update
     * @return Updated user information
     */
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser) {
        UpdateUser updatedUser = usersService.updateUser(updateUser);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Update avatar for authenticated user
     * @param image New avatar image
     * @return ResponseEntity with status
     */
    @PatchMapping("/me/image")
    public ResponseEntity<Void> updateUserImage(@RequestPart("image") MultipartFile image) {
        try {
            byte[] imageBytes = image.getBytes();
            boolean result = usersService.updateUserImage(imageBytes);
            return result ? ResponseEntity.ok().build() : ResponseEntity.status(500).build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
