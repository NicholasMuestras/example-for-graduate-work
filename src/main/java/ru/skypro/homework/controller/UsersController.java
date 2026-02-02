package ru.skypro.homework.controller;

import io.swagger.model.NewPassword;
import io.swagger.model.UpdateUser;
import io.swagger.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for user management
 * Provides endpoints for getting, updating user information and changing password
 */
@RestController
public interface UsersController {

    /**
     * Get information about authenticated user
     * @return User information
     */
    @GetMapping("/users/me")
    ResponseEntity<User> getUser();

    /**
     * Update password for authenticated user
     * @param newPassword New password data
     * @return ResponseEntity with status
     */
    @PostMapping("/users/set_password")
    ResponseEntity<Void> setPassword(@RequestBody NewPassword newPassword);

    /**
     * Update information about authenticated user
     * @param updateUser User data to update
     * @return Updated user information
     */
    @PatchMapping("/users/me")
    ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser);

    /**
     * Update avatar for authenticated user
     * @param image New avatar image
     * @return ResponseEntity with status
     */
    @PatchMapping("/users/me/image")
    ResponseEntity<Void> updateUserImage(@RequestPart("image") MultipartFile image);
}
