package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.dtos.request.FollowRequest;
import com.berryinkstamp.berrybackendservice.dtos.response.ConnectionStatus;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Follow;
import com.berryinkstamp.berrybackendservice.services.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/v1/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "Follow Profile", description = "Follow Profile")
    @PostMapping("/add")
    private Follow followProfile(@RequestBody @Valid FollowRequest followRequest, @RequestHeader(value = "profileType")ProfileType profileType){
        return followService.followProfile(followRequest, profileType);
    }

    @Operation(summary = "Unfollow Profile", description = "Unfollow Profile")
    @PostMapping("/remove")
    private Object unfollowProfile(@RequestBody @Valid FollowRequest followRequest, @RequestHeader(value = "profileType")ProfileType profileType){
        return followService.unfollowProfile(followRequest, profileType);
    }

    @Operation(summary = "Get Connection for a Profile", description = "Get Connection (Check if you are following a profile or not)")
    @GetMapping("/{profileId}")
    private ConnectionStatus getConnection(@RequestHeader(value = "profileType") ProfileType profileType, @PathVariable Long profileId){
        return followService.getConnectionStatus(profileId, profileType);
    }

}
