package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.entity.User;


/**
 * Mapper for converting between User entity and User DTO
 */
@Component
public class UserMapper {
    
    /**
     * Converts User entity to UpdateUser DTO
     * @param user User entity
     * @return UpdateUser DTO
     */
    public io.swagger.model.UpdateUser toUpdateUser(User user) {
        if (user == null) {
            return null;
        }

        io.swagger.model.UpdateUser updateUserDto = new io.swagger.model.UpdateUser();
        updateUserDto.setFirstName(user.getFirstName());
        updateUserDto.setLastName(user.getLastName());
        updateUserDto.setPhone(user.getPhone());

        return updateUserDto;
    }

    /**
     * Converts User entity to User DTO
     * @param user User entity
     * @return User DTO
     */
    public io.swagger.model.User toDto(User user) {
        if (user == null) {
            return null;
        }
        
        io.swagger.model.User userDto = new io.swagger.model.User();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhone(user.getPhone());
        userDto.setRole(io.swagger.model.User.RoleEnum.valueOf(user.getRole().name()));
        userDto.setImage(user.getImage());
        
        return userDto;
    }
    
    /**
     * Converts User DTO to User entity
     * @param userDto User DTO
     * @return User entity
     */
    public User toEntity(io.swagger.model.User userDto) {
        if (userDto == null) {
            return null;
        }
        
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setRole(User.Role.valueOf(userDto.getRole().name()));
        user.setImage(userDto.getImage());
        
        return user;
    }
}