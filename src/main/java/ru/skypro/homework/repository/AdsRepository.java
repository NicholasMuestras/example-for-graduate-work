package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Ads;

import java.util.List;

@Repository
public interface AdsRepository extends JpaRepository<Ads, Integer> {
    
    /**
     * Find all ads by author
     * @param author ads author
     * @return list of author's ads
     */
    List<Ads> findByAuthor(ru.skypro.homework.entity.User author);
    
    /**
     * Find ads by title fragment (case insensitive)
     * @param title title fragment
     * @return list of ads
     */
    List<Ads> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Find ads in price range
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of ads
     */
    List<Ads> findByPriceBetween(int minPrice, int maxPrice);
}
