package com.berryinkstamp.berrybackendservice.controllers.publicAPI;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.services.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Fetch all profile filter by profile type", description = "Fetch design by Id")
    @PostMapping
    public Page<Profile> getAllProfiles(@RequestParam ProfileType profile, Pageable pageable) {
        return profileService.getAllProfiles(profile, pageable);
    }

    @Operation(summary = "Fetch User profile", description = "Fetch User profile")
    @GetMapping("/{profileId}")
    public Profile getUserProfile(@PathVariable Long profileId) {
        return profileService.getUserProfile(profileId);
    }

}
