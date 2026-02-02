package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    // The loadUserByUsername method has been removed as the implementation has been moved to CustomUserDetailsService
    // UserRepository now contains only methods for working with the User entity
    
    /**
     * Find user by email
     * @param email user's email
     * @return Optional with user
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by email
     * @param email user's email
     * @return true if user exists
     */
    boolean existsByEmail(String email);
}
