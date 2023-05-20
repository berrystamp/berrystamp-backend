
package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.ReviewDto;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.exceptions.BadRequestException;
import com.berryinkstamp.berrybackendservice.exceptions.ForbiddenException;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.models.Rating;
import com.berryinkstamp.berrybackendservice.models.Review;
import com.berryinkstamp.berrybackendservice.repositories.ProfileRepository;
import com.berryinkstamp.berrybackendservice.repositories.RatingRepository;
import com.berryinkstamp.berrybackendservice.repositories.ReviewRepository;
import com.berryinkstamp.berrybackendservice.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {

    private final ReviewRepository reviewRepository;

    private final RatingRepository ratingRepository;

    private final ProfileRepository profileRepository;

    private final TokenProvider tokenProvider;

    @Value("${rating.weight.service:0.65}")
    private double serviceWeight;

    @Value("${rating.weight.communication:0.1}")
    private double communicationWeight;

    @Value("${rating.weight.recommendation:0.15}")
    private double recommendationWeight;

    @Value("${rating.weight.delivery:0.1}")
    private double deliveryWeight;

    @Override
    public Map<String, Object> getProfileRating(Long ProfileId) {
        Profile profile = profileRepository.findById(ProfileId).orElseThrow(() -> new NotFoundException("profile not found"));
        Rating rating = ratingRepository.findByProfile(profile).orElse(createRating(profile));
        List<Review> reviews = reviewRepository.findByRatedUser(profile);
        return Map.of("rating", rating, "reviews", reviews);
    }

    @Override
    public Review rateProfile(ReviewDto reviewDto, ProfileType profileType) {
        Profile ratedProfile = profileRepository.findById(reviewDto.getProfileId()).orElseThrow(() -> new NotFoundException("profile not found"));
        if (ratedProfile.getProfileType() == ProfileType.CUSTOMER) {
            throw new BadRequestException("this profile cannot be rated");
        }

        Profile ratingProfile = tokenProvider.getCurrentUser().getProfile(profileType);
        if (ratingProfile == null) {
            throw new ForbiddenException("no profile found with profile type");
        }

        if (ratingProfile == ratedProfile) {
            throw new BadRequestException("not allowed to rate yourself");
        }

        int star = calculateStar(reviewDto);

        Review review = new Review();
        review.setComment(reviewDto.getComment());
        review.setRatingUser(ratingProfile);
        review.setRatedUser(ratedProfile);
        review.setService(reviewDto.getService());
        review.setCommunication(reviewDto.getCommunication());
        review.setDeliveryTime(reviewDto.getDeliveryTime());
        review.setRecommendation(reviewDto.getRecommendation());
        review.setStars(star);
        review = reviewRepository.save(review);

        Rating rating = ratingRepository.findByProfile(ratedProfile).orElse(createRating(ratedProfile));
        rating.updateStarCount(star);
        ratingRepository.save(rating);

        return review;
    }

    private int calculateStar(ReviewDto reviewDto) {
        return (int) ((serviceWeight * reviewDto.getService()) + (recommendationWeight * reviewDto.getRecommendation()) + (communicationWeight * reviewDto.getCommunication()) + (deliveryWeight * reviewDto.getDeliveryTime()));
    }

    private Rating createRating(Profile profile) {
        Rating rating = ratingRepository.findByProfile(profile).orElse(new Rating());
        rating.setProfile(profile);
        rating = ratingRepository.save(rating);
        return rating;
    }


}
