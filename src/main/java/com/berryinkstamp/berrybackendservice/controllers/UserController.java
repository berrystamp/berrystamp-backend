package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateMailSettingRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdatePasswordRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateUserRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateUsernameRequest;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
@WrapApiResponse
public class UserController {

    private final UserService userService;

    @Operation(summary = "Change password", description = "Change Password")
    @PatchMapping("/password")
    public User updatePassword(@RequestBody @Valid UpdatePasswordRequest request) {
        return userService.updatePassword(request);
    }

    @Operation(summary = "Change username", description = "Change username")
    @PatchMapping("/username")
    public User updatePassword(@RequestBody @Valid UpdateUsernameRequest request) {
        return userService.updateUsername(request);
    }

    @Operation(summary = "Update mail setting", description = "Update mail setting")
    @PatchMapping("/mail-setting")
    public User updateMailSetting(@RequestBody @Valid UpdateMailSettingRequest request) {
        return userService.updateMailSetting(request);
    }

    @Operation(summary = "Update User", description = "Update User")
    @PutMapping
    public User updateUser(@RequestBody @Valid UpdateUserRequest request) {
        return userService.updateUser(request);
    }

    @Operation(summary = "Get User", description = "Get User")
    @GetMapping
    public User getUser() {
        return userService.getUser();
    }


}
