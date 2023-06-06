package com.berryinkstamp.berrybackendservice.controllers.admin;

import com.berryinkstamp.berrybackendservice.dtos.request.ReasonRequest;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.services.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin/profiles")
public class AdminAccountController {
    private ProfileService profileService;

    @Operation(summary = "Admin Fetch all profile")
    @PostMapping
    public Page<Profile> getAllProfiles(@RequestParam(required = false) ProfileType profile, @RequestParam(required = false) String field, Pageable pageable) {
        return profileService.adminGetAllProfiles(profile, field,  pageable);
    }

    @Operation(summary = "Fetch User profile", description = "Fetch User profile")
    @GetMapping("/{profileId}")
    public Profile getUserProfile(@PathVariable Long profileId) {
        return profileService.getUserProfile(profileId);
    }

    @Operation(summary = "Suspend User profile")
    @PatchMapping("/{profileId}/probation")
    public Profile suspendProfile(@PathVariable Long profileId, @RequestBody @Valid ReasonRequest request) {
        return profileService.suspendProfile(profileId, request);
    }

    @Operation(summary = "Terminate User profile")
    @PatchMapping("/{profileId}/termination")
    public Profile terminateProfile(@PathVariable Long profileId, @RequestBody @Valid ReasonRequest request) {
        return profileService.terminateProfile(profileId, request);
    }

    @Operation(summary = "Terminate User profile")
    @PatchMapping("/{profileId}/activation")
    public Profile activateProfile(@PathVariable Long profileId) {
        return profileService.activateProfile(profileId);
    }

}
