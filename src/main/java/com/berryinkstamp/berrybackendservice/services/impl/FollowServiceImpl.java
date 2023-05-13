
package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.FollowRequest;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.Follow;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.repositories.FollowRepository;
import com.berryinkstamp.berrybackendservice.repositories.ProfileRepository;
import com.berryinkstamp.berrybackendservice.services.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    private final ProfileRepository profileRepository;

    private final TokenProvider tokenProvider;


    @Override
    public List<Profile> getFollowersForProfile(Long profileId) {
        Profile following = profileRepository.findById(profileId).orElseThrow(() -> new NotFoundException("profile not found"));
        List<Follow> follows = followRepository.findByFollowing(following);
        return follows.stream().map(Follow::getFollowing).collect(Collectors.toList());
    }

    @Override
    public List<Profile> getFollowingsForProfile(Long profileId) {
        Profile follower = profileRepository.findById(profileId).orElseThrow(() -> new NotFoundException("profile not found"));
        List<Follow> follows = followRepository.findByFollower(follower);
        return follows.stream().map(Follow::getFollower).collect(Collectors.toList());
    }

    @Override
    public Follow followProfile(FollowRequest followRequest, ProfileType profileType) {
        Profile following = profileRepository.findById(followRequest.getFollowingProfileId()).orElseThrow(() -> new NotFoundException("profile not found"));
        Profile follower = tokenProvider.getCurrentUser().getProfile(profileType);
        Follow follow = followRepository.findFirstByFollowerAndFollowing(follower, following).orElse(null);
        if (Objects.isNull(follow)) {
            follow = new Follow();
        }
        follow.setFollower(follower);
        follow.setFollowing(following);
        follow = followRepository.save(follow);
        return follow;
    }

    @Override
    public Object unfollowProfile(FollowRequest followRequest, ProfileType profileType) {
        Profile following = profileRepository.findById(followRequest.getFollowingProfileId()).orElseThrow(() -> new NotFoundException("profile not found"));
        Profile follower = tokenProvider.getCurrentUser().getProfile(profileType);
        Follow follow = followRepository.findFirstByFollowerAndFollowing(follower, following).orElse(null);
        if (Objects.nonNull(follow)) {
            followRepository.delete(follow);
        }
        return Map.of();
    }
}
