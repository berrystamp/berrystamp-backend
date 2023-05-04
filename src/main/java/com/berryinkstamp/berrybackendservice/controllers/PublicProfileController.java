package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/public/profile")
@RestController
@RequiredArgsConstructor
@WrapApiResponse
public class PublicProfileController {

    private final ProfileService profileService;

    @PostMapping
    public Page<Profile> getAllProfiles(@RequestParam ProfileType profile, Pageable pageable) {
        return profileService.getAllProfiles(profile, pageable);
    }

    @GetMapping("/{userId}")
    public Profile getUserProfile(@PathVariable Long userId, @RequestParam ProfileType profile) {
        return profileService.getUserProfile(userId, profile);
    }

}
