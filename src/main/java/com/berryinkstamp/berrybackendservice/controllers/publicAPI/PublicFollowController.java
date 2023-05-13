package com.berryinkstamp.berrybackendservice.controllers.publicAPI;

import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.services.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/v1/api/public/follows")
@RequiredArgsConstructor
public class PublicFollowController {

    private final FollowService followService;

    @Operation(summary = "Get Followers For Profile", description = "Get Followers For Profile")
    @GetMapping("/follower/{profileId}")
    private List<Profile> getFollowersForProfile(@PathVariable Long profileId){
        return followService.getFollowersForProfile(profileId);
    }

    @Operation(summary = "Get Followings For Profile", description = "Get Followings For Profile")
    @GetMapping("/following/{profileId}")
    private List<Profile> getFollowingsForProfile(@PathVariable Long profileId){
        return followService.getFollowingsForProfile(profileId);
    }


}
