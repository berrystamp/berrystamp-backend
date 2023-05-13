package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.FollowRequest;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Follow;
import com.berryinkstamp.berrybackendservice.models.Profile;

import java.util.List;

public interface FollowService {
    List<Profile> getFollowersForProfile(Long userId);
    List<Profile> getFollowingsForProfile(Long userId);
    Follow followProfile(FollowRequest followRequest, ProfileType profileType);
    Object unfollowProfile(FollowRequest followRequest, ProfileType profile);
}
