package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.User;
import io.swagger.model.Ad;
import io.swagger.model.ExtendedAd;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Ads entity and DTO models
 */
@Component
public class AdsMapper {
    
    /**
     * Converts Ads entity to Ad DTO
     * @param ads Ads entity
     * @return Ad DTO
     */
    public Ad toAdDto(Ads ads) {
        if (ads == null) {
            return null;
        }
        
        Ad adDto = new Ad();
        adDto.setPk(ads.getPk());
        adDto.setTitle(ads.getTitle());
        adDto.setPrice(ads.getPrice());
        adDto.setImage(ads.getImage());
        adDto.setAuthor(ads.getAuthor().getId());
        
        return adDto;
    }
    
    /**
     * Converts Ads entity to ExtendedAd DTO
     * @param ads Ads entity
     * @return ExtendedAd DTO
     */
    public ExtendedAd toExtendedAdDto(Ads ads) {
        if (ads == null) {
            return null;
        }
        
        ExtendedAd extendedAdDto = new ExtendedAd();
        User author = ads.getAuthor();
        
        extendedAdDto.setPk(ads.getPk());
        extendedAdDto.setTitle(ads.getTitle());
        extendedAdDto.setPrice(ads.getPrice());
        extendedAdDto.setDescription(ads.getDescription());
        extendedAdDto.setImage(ads.getImage());
        
        extendedAdDto.setAuthorFirstName(author.getFirstName());
        extendedAdDto.setAuthorLastName(author.getLastName());
        extendedAdDto.setEmail(author.getEmail());
        extendedAdDto.setPhone(author.getPhone());
        
        return extendedAdDto;
    }
    
    /**
     * Converts list of Ads entities to Ads DTO
     * @param adsList list of Ads entities
     * @return Ads DTO
     */
    public io.swagger.model.Ads toAdsDto(List<Ads> adsList) {
        if (adsList == null) {
            return null;
        }

        io.swagger.model.Ads adsDto = new io.swagger.model.Ads();
        adsDto.setCount(adsList.size());
        adsDto.setResults(adsList.stream()
                .map(this::toAdDto)
                .collect(Collectors.toList()));
        
        return adsDto;
    }
}
