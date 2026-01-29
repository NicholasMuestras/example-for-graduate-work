package ru.skypro.homework.service;

import io.swagger.model.NewPassword;
import io.swagger.model.UpdateUser;
import io.swagger.model.User;

/**
 * Service interface for user management operations.
 * Provides methods for retrieving, updating user information and changing password.
 */
public interface UsersService {

    /**
     * Get information about the authenticated user.
     * @return User information
     */
    User getUser();

    /**
     * Update password for the authenticated user.
     * @param newPassword New password data containing current and new password
     * @return true if password was successfully updated, false otherwise
     */
    boolean setPassword(NewPassword newPassword);

    /**
     * Update information about the authenticated user.
     * @param updateUser User data to update (first name, last name, phone)
     * @return Updated user information
     */
    UpdateUser updateUser(UpdateUser updateUser);

    /**
     * Update avatar for the authenticated user.
     * @param image New avatar image as byte array
     * @return true if avatar was successfully updated, false otherwise
     */
    boolean updateUserImage(byte[] image);
}
