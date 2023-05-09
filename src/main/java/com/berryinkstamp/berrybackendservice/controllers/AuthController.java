package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.BaseRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.LoginRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.RegistrationRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.ResetPasswordRequest;
import com.berryinkstamp.berrybackendservice.dtos.response.LoginResponse;
import com.berryinkstamp.berrybackendservice.dtos.response.RegistrationResponse;
import com.berryinkstamp.berrybackendservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@WrapApiResponse
@RequiredArgsConstructor
public class AuthController {
   private final UserService userService;

   @Operation(summary = "Register user", description = "Register user")
   @PostMapping(value = "/register")
   public RegistrationResponse userRegistration(@RequestBody @Valid RegistrationRequest dto) {
      return userService.registerUser(dto);
   }

   @Operation(summary = "Resend OTP", description = "Resend OTP")
   @PostMapping(value = "/resend-code")
   public Object resendCode(@RequestBody @Valid BaseRequest baseRequest) {
      return userService.resendCode(baseRequest);
   }

   @Operation(summary = "Activate Account", description = "Activate Account")
   @PatchMapping(value = "/activate/{otp}")
   public LoginResponse userActivation(@PathVariable String otp, @RequestBody @Valid BaseRequest baseRequest) {
      return userService.activateAccount(otp, baseRequest);
   }

   @Operation(summary = "Validate OTP", description = "Validate if OTP is correct")
   @PostMapping(value = "/validate-code/{otp}")
   public Object validateCode(@PathVariable String otp, @RequestBody @Valid BaseRequest baseRequest) {
      return userService.validateCode(otp, baseRequest);
   }

   @Operation(summary = "Login to all profile", description = "Login to all profile")
   @PostMapping("/login")
   public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
      return userService.login(loginRequest);
   }


   @Operation(summary = "Forget Password - Send OTP", description = "Forget Password - Send OTP")
   @PostMapping(value = "/reset-password")
   public Object resetPassword(@RequestBody @Valid BaseRequest baseRequest) {
      return userService.resetPassword(baseRequest);
   }

   @Operation(summary = "Forget Password - Reset Password", description = "Forget Password - Reset Password")
   @PatchMapping(value = "/reset-password")
   public Object completeResetPassword(@RequestBody @Valid ResetPasswordRequest request) {
      return userService.completeResetPassword(request);
   }

   @Operation(summary = "Check App Status", description = "Check App Status")
   @GetMapping(value = "/status")
   public Object test() {
      return Map.of("status", "success");
   }


}
