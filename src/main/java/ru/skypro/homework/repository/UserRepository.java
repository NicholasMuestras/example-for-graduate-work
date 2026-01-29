package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    /**
     * Найти пользователя по email
     * @param email email пользователя
     * @return Optional с пользователем
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Проверить существование пользователя по email
     * @param email email пользователя
     * @return true если пользователь существует
     */
    boolean existsByEmail(String email);
}
