package ru.skypro.homework.service.impl;

import io.swagger.model.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

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
        User user = getCurrentUser();
        
        Ads ad = new Ads();
        ad.setTitle(properties.getTitle());
        ad.setPrice(properties.getPrice());
        ad.setDescription(properties.getDescription());
        ad.setAuthor(user);
        
        String imagePath = saveImage(image);
        ad.setImage(imagePath);
        
        Ads savedAd = adsRepository.save(ad);
        
        return adsMapper.toAdDto(savedAd);
    }

    @Override
    public io.swagger.model.Comment addComment(Integer id, CreateOrUpdateComment body) {
        User user = getCurrentUser();
        
        Ads ad = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        Comment comment = new Comment();
        comment.setText(body.getText());
        comment.setAuthor(user);
        comment.setAd(ad);
        comment.setCreatedAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        
        Comment savedComment = commentRepository.save(comment);
        
        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    public boolean deleteComment(Integer adId, Integer commentId) {
        User currentUser = getCurrentUser();
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        
        Ads ad = adsRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        if (!comment.getAd().getPk().equals(adId)) {
            throw new IllegalArgumentException("Comment does not belong to this advertisement");
        }
        
        if (!comment.getAuthor().getId().equals(currentUser.getId()) 
                && !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new SecurityException("You don't have permission to delete this comment");
        }
        
        commentRepository.delete(comment);
        return true;
    }

    @Override
    public ExtendedAd getAds(Integer id) {
        Ads ad = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        return adsMapper.toExtendedAdDto(ad);
    }

    @Override
    public io.swagger.model.Ads getAdsMe() {
        User user = getCurrentUser();
        List<Ads> userAds = adsRepository.findByAuthor(user);
        return adsMapper.toAdsDto(userAds);
    }

    @Override
    public io.swagger.model.Ads getAllAds() {
        List<Ads> allAds = adsRepository.findAll();
        
        return adsMapper.toAdsDto(allAds);
    }

    @Override
    public Comments getComments(Integer id) {
        Ads ad = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        List<Comment> comments = commentRepository.findByAd(ad);
        
        return commentMapper.toCommentsDto(comments);
    }

    @Override
    public boolean removeAd(Integer id) {
        User currentUser = getCurrentUser();
        
        Ads ad = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        if (!ad.getAuthor().getId().equals(currentUser.getId()) 
                && !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new SecurityException("You don't have permission to delete this advertisement");
        }
        
        List<Comment> comments = commentRepository.findByAd(ad);
        commentRepository.deleteAll(comments);
        
        deleteImage(ad.getImage());
        
        adsRepository.delete(ad);
        return true;
    }

    @Override
    public Ad updateAds(Integer id, CreateOrUpdateAd body) {
        User currentUser = getCurrentUser();
        
        Ads ad = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        if (!ad.getAuthor().getId().equals(currentUser.getId()) 
                && !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new SecurityException("You don't have permission to update this advertisement");
        }
        
        ad.setTitle(body.getTitle());
        ad.setPrice(body.getPrice());
        ad.setDescription(body.getDescription());
        
        Ads updatedAd = adsRepository.save(ad);
        
        return adsMapper.toAdDto(updatedAd);
    }

    @Override
    public io.swagger.model.Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment body) {
        User currentUser = getCurrentUser();
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        
        Ads ad = adsRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        if (!comment.getAd().getPk().equals(adId)) {
            throw new IllegalArgumentException("Comment does not belong to this advertisement");
        }
        
        if (!comment.getAuthor().getId().equals(currentUser.getId()) 
                && !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new SecurityException("You don't have permission to update this comment");
        }
        
        comment.setText(body.getText());
        
        Comment updatedComment = commentRepository.save(comment);
        
        return commentMapper.toCommentDto(updatedComment);
    }

    @Override
    public Resource updateImage(Integer id, MultipartFile image) {
        User currentUser = getCurrentUser();
        Ads ad = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
        
        if (!ad.getAuthor().getId().equals(currentUser.getId())
                && !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new SecurityException("You don't have permission to update this advertisement image");
        }
        
        String imagePath = saveImage(image);
        ad.setImage(imagePath);
        
        adsRepository.save(ad);
        
        try {
            Path imagePathObj = Paths.get(imagePath.substring(1));
            return new UrlResource(imagePathObj.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to create resource", e);
        }
    }

    // Helper methods
    
    /**
     * Retrieves the currently authenticated user
     * @return User entity of the current user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    
    /**
     * Saves image file to disk
     * @param image image file to save
     * @return path to the saved image
     */
    private String saveImage(MultipartFile image) {
        try {
            Path uploadDir = Paths.get(IMAGES_DIR);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            
            Files.copy(image.getInputStream(), filePath);
            
            return "/" + IMAGES_DIR + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }
    
    /**
     * Deletes image file from disk
     * @param imagePath path to the image to delete
     */
    private void deleteImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Path imagePathObj = Paths.get(imagePath.substring(1));
                if (Files.exists(imagePathObj)) {
                    Files.delete(imagePathObj);
                }
            } catch (IOException e) {
                System.err.println("Failed to delete image: " + e.getMessage());
            }
        }
    }
}
