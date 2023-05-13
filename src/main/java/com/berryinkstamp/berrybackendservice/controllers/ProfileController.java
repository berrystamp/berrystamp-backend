package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.AddProfileRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateProfileRequest;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.services.ProfileService;
import com.berryinkstamp.berrybackendservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/v1/profile")
@RestController
@RequiredArgsConstructor
@WrapApiResponse
public class ProfileController {

    private final UserService userService;

    private final ProfileService profileService;

    @Operation(summary = "Add a profile", description = "Add a profile")
    @PostMapping
    public User addProfile(@RequestBody @Valid AddProfileRequest request) {
        return userService.addProfile(request);
    }

    @Operation(summary = "Update a profile", description = "Update a profile")
    @PutMapping
    public Profile updateProfile(@RequestBody @Valid UpdateProfileRequest request, @RequestHeader(value = "profileType") ProfileType profileType) {
        return profileService.updateProfile(request, profileType);
    }

}
