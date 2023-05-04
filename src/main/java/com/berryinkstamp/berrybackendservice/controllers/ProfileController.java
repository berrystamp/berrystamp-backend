package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.AddProfileRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateMailSettingRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdatePasswordRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateProfileRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateUserRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateUsernameRequest;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.services.ProfileService;
import com.berryinkstamp.berrybackendservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/profile")
@RestController
@RequiredArgsConstructor
@WrapApiResponse
public class ProfileController {

    private final UserService userService;

    private final ProfileService profileService;

    @PostMapping
    public User addProfile(@RequestBody @Valid AddProfileRequest request) {
        return userService.addProfile(request);
    }

    @PutMapping
    public Profile updateProfile(@RequestBody @Valid UpdateProfileRequest request) {
        return profileService.updateProfile(request);
    }

}
