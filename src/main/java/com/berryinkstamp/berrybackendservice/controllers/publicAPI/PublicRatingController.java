package com.berryinkstamp.berrybackendservice.controllers.publicAPI;

import com.berryinkstamp.berrybackendservice.services.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/v1/api/public/ratings")
@RequiredArgsConstructor
public class PublicRatingController {

    private final RatingService ratingService;

    @Operation(summary = "Get Profile Rating", description = "Get Profile Rating")
    @GetMapping("/{profileId}")
    private Map<String, Object> getFollowersForProfile(@PathVariable Long profileId){
        return ratingService.getProfileRating(profileId);
    }


}
