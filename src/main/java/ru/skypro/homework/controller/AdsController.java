package ru.skypro.homework.controller;

import io.swagger.model.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for advertisement management
 * Provides endpoints for CRUD operations with advertisements and comments
 */
@RestController
public interface AdsController {

    /**
     * Add a new advertisement
     * @param properties Advertisement properties
     * @param image Advertisement image
     * @return Created advertisement
     */
    @PostMapping("/ads")
    ResponseEntity<Ad> addAd(
            @RequestPart("properties") CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile image
    );

    /**
     * Add a comment to advertisement
     * @param id Advertisement ID
     * @param body Comment data
     * @return Created comment
     */
    @PostMapping("/ads/{id}/comments")
    ResponseEntity<Comment> addComment(
            @PathVariable("id") Integer id,
            @RequestBody CreateOrUpdateComment body
    );

    /**
     * Delete a comment
     * @param adId Advertisement ID
     * @param commentId Comment ID
     * @return ResponseEntity with status
     */
    @DeleteMapping("/ads/{adId}/comments/{commentId}")
    ResponseEntity<Void> deleteComment(
            @PathVariable("adId") Integer adId,
            @PathVariable("commentId") Integer commentId
    );

    /**
     * Get information about advertisement
     * @param id Advertisement ID
     * @return Advertisement details
     */
    @GetMapping("/ads/{id}")
    ResponseEntity<ExtendedAd> getAds(@PathVariable("id") Integer id);

    /**
     * Get advertisements of authenticated user
     * @return User's advertisements
     */
    @GetMapping("/ads/me")
    ResponseEntity<Ads> getAdsMe();

    /**
     * Get all advertisements
     * @return All advertisements
     */
    @GetMapping("/ads")
    ResponseEntity<Ads> getAllAds();

    /**
     * Get comments of advertisement
     * @param id Advertisement ID
     * @return Comments of advertisement
     */
    @GetMapping("/ads/{id}/comments")
    ResponseEntity<Comments> getComments(@PathVariable("id") Integer id);

    /**
     * Delete advertisement
     * @param id Advertisement ID
     * @return ResponseEntity with status
     */
    @DeleteMapping("/ads/{id}")
    ResponseEntity<Void> removeAd(@PathVariable("id") Integer id);

    /**
     * Update advertisement information
     * @param id Advertisement ID
     * @param body Advertisement data to update
     * @return Updated advertisement
     */
    @PatchMapping("/ads/{id}")
    ResponseEntity<Ad> updateAds(
            @PathVariable("id") Integer id,
            @RequestBody CreateOrUpdateAd body
    );

    /**
     * Update comment
     * @param adId Advertisement ID
     * @param commentId Comment ID
     * @param body Comment data to update
     * @return Updated comment
     */
    @PatchMapping("/ads/{adId}/comments/{commentId}")
    ResponseEntity<Comment> updateComment(
            @PathVariable("adId") Integer adId,
            @PathVariable("commentId") Integer commentId,
            @RequestBody CreateOrUpdateComment body
    );

    /**
     * Update advertisement image
     * @param id Advertisement ID
     * @param image New image
     * @return Updated image
     */
    @PatchMapping("/ads/{id}/image")
    ResponseEntity<Resource> updateImage(
            @PathVariable("id") Integer id,
            @RequestPart("image") MultipartFile image
    );
}
