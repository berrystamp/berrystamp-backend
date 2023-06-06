package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.ReasonRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateProfileRequest;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileService {

    Page<Profile> getAllProfiles(ProfileType profileType, Pageable pageable);

    Profile getUserProfile(Long userId);

    Profile updateProfile(UpdateProfileRequest request, ProfileType profileType);

    Page<Profile> adminGetAllProfiles(ProfileType profile, String field, Pageable pageable);

    Profile suspendProfile(Long profileId, ReasonRequest request);

    Profile terminateProfile(Long profileId, ReasonRequest request);

    Profile activateProfile(Long profileId);
}
