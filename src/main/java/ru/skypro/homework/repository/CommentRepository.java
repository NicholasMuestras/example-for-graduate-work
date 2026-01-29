package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
    /**
     * Найти все комментарии по объявлению
     * @param ad объявление
     * @return список комментариев объявления
     */
    List<Comment> findByAd(ru.skypro.homework.entity.Ads ad);
    
    /**
     * Найти комментарии по автору
     * @param author автор комментариев
     * @return список комментариев автора
     */
    List<Comment> findByAuthor(ru.skypro.homework.entity.User author);
    
    /**
     * Найти комментарии по автору и объявлению
     * @param author автор комментария
     * @param ad объявление
     * @return список комментариев
     */
    List<Comment> findByAuthorAndAd(ru.skypro.homework.entity.User author, ru.skypro.homework.entity.Ads ad);
    
    /**
     * Проверить существование комментария по автору и объявлению
     * @param author автор комментария
     * @param ad объявление
     * @return true если комментарий существует
     */
    boolean existsByAuthorAndAd(ru.skypro.homework.entity.User author, ru.skypro.homework.entity.Ads ad);
}
