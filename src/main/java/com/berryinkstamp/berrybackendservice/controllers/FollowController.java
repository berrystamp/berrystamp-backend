package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.dtos.request.FollowRequest;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Follow;
import com.berryinkstamp.berrybackendservice.services.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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


}
