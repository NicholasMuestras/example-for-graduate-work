package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Ads;

import java.util.List;

@Repository
public interface AdsRepository extends JpaRepository<Ads, Integer> {
    
    /**
     * Найти все объявления по автору
     * @param author автор объявлений
     * @return список объявлений автора
     */
    List<Ads> findByAuthor(ru.skypro.homework.entity.User author);
    
    /**
     * Найти объявления по фрагменту заголовка (игнорируя регистр)
     * @param title фрагмент заголовка
     * @return список объявлений
     */
    List<Ads> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Найти объявления в диапазоне цен
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @return список объявлений
     */
    List<Ads> findByPriceBetween(int minPrice, int maxPrice);
}
