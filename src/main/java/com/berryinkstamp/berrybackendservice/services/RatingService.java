package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.ReviewDto;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Review;

import java.util.Map;

public interface RatingService {
    Map<String, Object> getProfileRating(Long ProfileId);
    Review rateProfile(ReviewDto reviewDto, ProfileType profileType);
}
