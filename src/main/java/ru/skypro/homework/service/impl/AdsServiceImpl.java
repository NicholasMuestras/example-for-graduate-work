package ru.skypro.homework.service.impl;

import io.swagger.model.*;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class AdsServiceImpl implements AdsService {

    private static final String IMAGES_DIR = "images/";

    private final AdsRepository adsRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdsMapper adsMapper;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    public AdsServiceImpl(
        AdsRepository adsRepository,
        CommentRepository commentRepository,
        UserRepository userRepository,
        AdsMapper adsMapper,
        CommentMapper commentMapper,
        UserMapper userMapper
    ) {
        this.adsRepository = adsRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.adsMapper = adsMapper;
        this.commentMapper = commentMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image) {
        // Получаем текущего пользователя
        User user = getCurrentUser();
        
        // Создаем новое объявление
        Ads ad = new Ads();
        ad.setTitle(properties.getTitle());
        ad.setPrice(properties.getPrice());
        ad.setDescription(properties.getDescription());
        ad.setAuthor(user);
        
        // Сохраняем изображение
        String imagePath = saveImage(image);
        ad.setImage(imagePath);
        
        // Сохраняем объявление в базе данных
        Ads savedAd = adsRepository.save(ad);
        
        // Конвертируем в DTO и возвращаем
        return adsMapper.toAdDto(savedAd);
    }

    @Override
    public io.swagger.model.Comment addComment(Integer id, CreateOrUpdateComment body) {
        // Получаем текущего пользователя
        User user = getCurrentUser();
        
        // Находим объявление по ID
        Ads ad = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        // Создаем новый комментарий
        Comment comment = new Comment();
        comment.setText(body.getText());
        comment.setAuthor(user);
        comment.setAd(ad);
        comment.setCreatedAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        
        // Сохраняем комментарий в базе данных
        Comment savedComment = commentRepository.save(comment);
        
        // Конвертируем в DTO и возвращаем
        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    public boolean deleteComment(Integer adId, Integer commentId) {
        // Получаем текущего пользователя
        User currentUser = getCurrentUser();
        
        // Находим комментарий по ID и ID объявления
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        
        // Проверяем, что объявление существует
        Ads ad = adsRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        // Проверяем, что комментарий принадлежит объявлению
        if (!comment.getAd().getPk().equals(adId)) {
            throw new IllegalArgumentException("Comment does not belong to this advertisement");
        }
        
        // Проверяем, что пользователь является автором комментария или администратором
        if (!comment.getAuthor().getId().equals(currentUser.getId()) 
                && !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new SecurityException("You don't have permission to delete this comment");
        }
        
        // Удаляем комментарий
        commentRepository.delete(comment);
        return true;
    }

    @Override
    public ExtendedAd getAds(Integer id) {
        // Находим объявление по ID
        Ads ad = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        // Конвертируем в ExtendedAd DTO и возвращаем
        return adsMapper.toExtendedAdDto(ad);
    }

    @Override
    public io.swagger.model.Ads getAdsMe() {
        // Получаем текущего пользователя
        User user = getCurrentUser();
        
        // Находим все объявления пользователя
        List<Ads> userAds = adsRepository.findByAuthor(user);
        
        // Конвертируем в DTO и возвращаем
        return adsMapper.toAdsDto(userAds);
    }

    @Override
    public io.swagger.model.Ads getAllAds() {
        // Получаем все объявления из базы данных
        List<Ads> allAds = adsRepository.findAll();
        
        // Конвертируем в DTO и возвращаем
        return adsMapper.toAdsDto(allAds);
    }

    @Override
    public Comments getComments(Integer id) {
        // Находим объявление по ID
        Ads ad = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        // Получаем все комментарии для объявления
        List<Comment> comments = commentRepository.findByAd(ad);
        
        // Конвертируем в DTO и возвращаем
        return commentMapper.toCommentsDto(comments);
    }

    @Override
    public boolean removeAd(Integer id) {
        // Получаем текущего пользователя
        User currentUser = getCurrentUser();
        
        // Находим объявление по ID
        Ads ad = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        // Проверяем, что пользователь является автором объявления или администратором
        if (!ad.getAuthor().getId().equals(currentUser.getId()) 
                && !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new SecurityException("You don't have permission to delete this advertisement");
        }
        
        // Удаляем все комментарии объявления
        List<Comment> comments = commentRepository.findByAd(ad);
        commentRepository.deleteAll(comments);
        
        // Удаляем изображение объявления
        deleteImage(ad.getImage());
        
        // Удаляем объявление
        adsRepository.delete(ad);
        return true;
    }

    @Override
    public Ad updateAds(Integer id, CreateOrUpdateAd body) {
        // Получаем текущего пользователя
        User currentUser = getCurrentUser();
        
        // Находим объявление по ID
        Ads ad = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        // Проверяем, что пользователь является автором объявления или администратором
        if (!ad.getAuthor().getId().equals(currentUser.getId()) 
                && !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new SecurityException("You don't have permission to update this advertisement");
        }
        
        // Обновляем свойства объявления
        ad.setTitle(body.getTitle());
        ad.setPrice(body.getPrice());
        ad.setDescription(body.getDescription());
        
        // Сохраняем обновленное объявление в базе данных
        Ads updatedAd = adsRepository.save(ad);
        
        // Конвертируем в DTO и возвращаем
        return adsMapper.toAdDto(updatedAd);
    }

    @Override
    public io.swagger.model.Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment body) {
        // Получаем текущего пользователя
        User currentUser = getCurrentUser();
        
        // Находим комментарий по ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        
        // Проверяем, что объявление существует
        Ads ad = adsRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        // Проверяем, что комментарий принадлежит объявлению
        if (!comment.getAd().getPk().equals(adId)) {
            throw new IllegalArgumentException("Comment does not belong to this advertisement");
        }
        
        // Проверяем, что пользователь является автором комментария или администратором
        if (!comment.getAuthor().getId().equals(currentUser.getId()) 
                && !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new SecurityException("You don't have permission to update this comment");
        }
        
        // Обновляем текст комментария
        comment.setText(body.getText());
        
        // Сохраняем обновленный комментарий в базе данных
        Comment updatedComment = commentRepository.save(comment);
        
        // Конвертируем в DTO и возвращаем
        return commentMapper.toCommentDto(updatedComment);
    }

    @Override
    public Resource updateImage(Integer id, MultipartFile image) {
        // Получаем текущего пользователя
        User currentUser = getCurrentUser();
        
        // Находим объявление по ID
        Ads ad = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        // Проверяем, что пользователь является автором объявления или администратором
        if (!ad.getAuthor().getId().equals(currentUser.getId()) 
                && !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new SecurityException("You don't have permission to update this advertisement image");
        }
        
        // Удаляем старое изображение
        deleteImage(ad.getImage());
        
        // Сохраняем новое изображение
        String imagePath = saveImage(image);
        ad.setImage(imagePath);
        
        // Сохраняем объявление с новым изображением
        adsRepository.save(ad);
        
        // Возвращаем путь к изображению (в реальном приложении здесь должен быть код для возврата Resource)
        // Для простоты возвращаем null, так как работа с Resource требует дополнительной настройки
        return null;
    }

    // Вспомогательные методы
    
    /**
     * Получает текущего аутентифицированного пользователя
     * @return User entity текущего пользователя
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    
    /**
     * Сохраняет изображение на диск
     * @param image файл изображения
     * @return путь к сохраненному изображению
     */
    private String saveImage(MultipartFile image) {
        try {
            // Создаем директорию для изображений, если она не существует
            Path uploadDir = Paths.get(IMAGES_DIR);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // Генерируем уникальное имя файла
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            
            // Сохраняем файл
            Files.copy(image.getInputStream(), filePath);
            
            // Возвращаем относительный путь
            return "/" + IMAGES_DIR + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }
    
    /**
     * Удаляет изображение с диска
     * @param imagePath путь к изображению
     */
    private void deleteImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Path imagePathObj = Paths.get(imagePath.substring(1)); // Убираем первый слеш
                if (Files.exists(imagePathObj)) {
                    Files.delete(imagePathObj);
                }
            } catch (IOException e) {
                System.err.println("Failed to delete image: " + e.getMessage());
            }
        }
    }
}
