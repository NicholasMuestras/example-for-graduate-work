package ru.skypro.homework.controller;

import io.swagger.model.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * REST controller for advertisement management
 * Provides endpoints for CRUD operations with advertisements and comments
 */
@RestController
@CrossOrigin("http://localhost:3000")
public class AdsController {

    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    /**
     * Add a new advertisement
     * @param properties Advertisement properties
     * @param image Advertisement image
     * @return Created advertisement
     */
    @PostMapping("/ads")
    public ResponseEntity<Ad> addAd(
            @RequestPart("properties") CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile image,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        Ad ad = adsService.addAd(properties, image);
        return ResponseEntity.ok(ad);
    }

    /**
     * Add a comment to advertisement
     * @param id Advertisement ID
     * @param body Comment data
     * @return Created comment
     */
    @PostMapping("/ads/{id}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable("id") Integer id,
            @RequestBody CreateOrUpdateComment body,
            Authentication authentication
    ) {
        Comment comment = adsService.addComment(id, body);
        return ResponseEntity.ok(comment);
    }

    /**
     * Delete a comment
     * @param adId Advertisement ID
     * @param commentId Comment ID
     * @return ResponseEntity with status
     */
    @DeleteMapping("/ads/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("adId") Integer adId,
            @PathVariable("commentId") Integer commentId,
            Authentication authentication
    ) {
        boolean deleted = adsService.deleteComment(adId, commentId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get information about advertisement
     * @param id Advertisement ID
     * @return Advertisement details
     */
    @GetMapping("/ads/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable("id") Integer id) {
        ExtendedAd ad = adsService.getAds(id);
        return ResponseEntity.ok(ad);
    }

    /**
     * Get advertisements of authenticated user
     * @return User's advertisements
     */
    @GetMapping("/ads/me")
    public ResponseEntity<Ads> getAdsMe() {
        Ads ads = adsService.getAdsMe();
        return ResponseEntity.ok(ads);
    }

    /**
     * Get all advertisements
     * @return All advertisements
     */
    @GetMapping("/ads")
    public ResponseEntity<Ads> getAllAds() {
        Ads ads = adsService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    /**
     * Get comments of advertisement
     * @param id Advertisement ID
     * @return Comments of advertisement
     */
    @GetMapping("/ads/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable("id") Integer id) {
        Comments comments = adsService.getComments(id);
        return ResponseEntity.ok(comments);
    }

    /**
     * Delete advertisement
     * @param id Advertisement ID
     * @return ResponseEntity with status
     */
    @DeleteMapping("/ads/{id}")
    public ResponseEntity<Void> removeAd(
            @PathVariable("id") Integer id,
            Authentication authentication
    ) {
        boolean deleted = adsService.removeAd(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Update advertisement information
     * @param id Advertisement ID
     * @param body Advertisement data to update
     * @return Updated advertisement
     */
    @PatchMapping("/ads/{id}")
    public ResponseEntity<Ad> updateAds(
            @PathVariable("id") Integer id,
            @RequestBody CreateOrUpdateAd body,
            Authentication authentication
    ) {
        Ad ad = adsService.updateAds(id, body);
        return ResponseEntity.ok(ad);
    }

    /**
     * Update comment
     * @param adId Advertisement ID
     * @param commentId Comment ID
     * @param body Comment data to update
     * @return Updated comment
     */
    @PatchMapping("/ads/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable("adId") Integer adId,
            @PathVariable("commentId") Integer commentId,
            @RequestBody CreateOrUpdateComment body,
            Authentication authentication
    ) {
        Comment comment = adsService.updateComment(adId, commentId, body);
        return ResponseEntity.ok(comment);
    }

    /**
     * Update advertisement image
     * @param id Advertisement ID
     * @param image New image
     * @return Updated image
     */
    @PatchMapping("/ads/{id}/image")
    public ResponseEntity<Resource> updateImage(
            @PathVariable("id") Integer id,
            @RequestPart("image") MultipartFile image
    ) {
        Resource resource = adsService.updateImage(id, image);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get("images", filename);
            Resource resource = new UrlResource(imagePath.toUri());
            
            if (resource.exists()) {
                String contentType = Files.probeContentType(imagePath);
                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
