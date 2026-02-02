package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
    /**
     * Find all comments by ad
     * @param ad advertisement
     * @return list of ad's comments
     */
    List<Comment> findByAd(ru.skypro.homework.entity.Ads ad);
    
    /**
     * Find comments by author
     * @param author comments author
     * @return list of author's comments
     */
    List<Comment> findByAuthor(ru.skypro.homework.entity.User author);
    
    /**
     * Find comments by author and ad
     * @param author comment author
     * @param ad advertisement
     * @return list of comments
     */
    List<Comment> findByAuthorAndAd(ru.skypro.homework.entity.User author, ru.skypro.homework.entity.Ads ad);
    
    /**
     * Check if comment exists by author and ad
     * @param author comment author
     * @param ad advertisement
     * @return true if comment exists
     */
    boolean existsByAuthorAndAd(ru.skypro.homework.entity.User author, ru.skypro.homework.entity.Ads ad);
}
