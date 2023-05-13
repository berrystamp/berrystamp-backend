package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateProfileRequest;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.repositories.ProfileRepository;
import com.berryinkstamp.berrybackendservice.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final TokenProvider tokenProvider;


    @Override
    public Page<Profile> getAllProfiles(ProfileType profileType, Pageable pageable) {
        return profileRepository.findByProfileType(profileType, pageable);
    }

    @Override
    public Profile getUserProfile(Long profileId) {
        Optional<Profile> profile = profileRepository.findById(profileId);
        if (profile.isEmpty()) {
            throw new NotFoundException("user profile not found");
        }
        return profile.get();
    }

    @Override
    public Profile updateProfile(UpdateProfileRequest request, ProfileType profileType) {
        Profile profile = tokenProvider.getCurrentUser().getProfile(profileType);
        if (profile == null) {
            throw new NotFoundException("profile not found");
        }

        profile.setName(request.getName());
        profile.setBio(request.getBio());
        profile.setCategories(String.join(",", request.getCategories()));
        profile.setProfilePic(request.getProfilePic());
        profile.setCoverPic(request.getCoverPic());
        profile = profileRepository.save(profile);
        return profile;
    }

}
