package ru.skypro.homework.service;

import io.swagger.model.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for advertisement management
 * Provides business logic for CRUD operations with advertisements and comments
 */
public interface AdsService {

    /**
     * Add a new advertisement
     * @param properties Advertisement properties
     * @param image Advertisement image
     * @return Created advertisement
     */
    Ad addAd(CreateOrUpdateAd properties, MultipartFile image);

    /**
     * Add a comment to advertisement
     * @param id Advertisement ID
     * @param body Comment data
     * @return Created comment
     */
    Comment addComment(Integer id, CreateOrUpdateComment body);

    /**
     * Delete a comment
     * @param adId Advertisement ID
     * @param commentId Comment ID
     * @return true if comment was deleted successfully
     */
    boolean deleteComment(Integer adId, Integer commentId);

    /**
     * Get information about advertisement
     * @param id Advertisement ID
     * @return Advertisement details
     */
    ExtendedAd getAds(Integer id);

    /**
     * Get advertisements of authenticated user
     * @return User's advertisements
     */
    Ads getAdsMe();

    /**
     * Get all advertisements
     * @return All advertisements
     */
    Ads getAllAds();

    /**
     * Get comments of advertisement
     * @param id Advertisement ID
     * @return Comments of advertisement
     */
    Comments getComments(Integer id);

    /**
     * Delete advertisement
     * @param id Advertisement ID
     * @return true if advertisement was deleted successfully
     */
    boolean removeAd(Integer id);

    /**
     * Update advertisement information
     * @param id Advertisement ID
     * @param body Advertisement data to update
     * @return Updated advertisement
     */
    Ad updateAds(Integer id, CreateOrUpdateAd body);

    /**
     * Update comment
     * @param adId Advertisement ID
     * @param commentId Comment ID
     * @param body Comment data to update
     * @return Updated comment
     */
    Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment body);

    /**
     * Update advertisement image
     * @param id Advertisement ID
     * @param image New image
     * @return Updated image
     */
    Resource updateImage(Integer id, MultipartFile image);
}
