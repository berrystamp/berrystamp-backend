package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.BaseRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.LoginRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.RegistrationRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.ResetPasswordRequest;
import com.berryinkstamp.berrybackendservice.dtos.response.LoginResponse;
import com.berryinkstamp.berrybackendservice.dtos.response.RegistrationResponse;
import com.berryinkstamp.berrybackendservice.services.UserService;
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

   @PostMapping(value = "/register")
   public RegistrationResponse userRegistration(@RequestBody @Valid RegistrationRequest dto) {
      return userService.registerUser(dto);
   }

   @PostMapping(value = "/resend-code")
   public Object resendCode(@RequestBody @Valid BaseRequest baseRequest) {
      return userService.resendCode(baseRequest);
   }

   @PatchMapping(value = "/activate/{otp}")
   public LoginResponse userActivation(@PathVariable String otp, @RequestBody @Valid BaseRequest baseRequest) {
      return userService.activateAccount(otp, baseRequest);
   }

   @PostMapping(value = "/validate-code/{otp}")
   public Object validateCode(@PathVariable String otp, @RequestBody @Valid BaseRequest baseRequest) {
      return userService.validateCode(otp, baseRequest);
   }

   @PostMapping("/login")
   public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
      return userService.login(loginRequest);
   }


   @PostMapping(value = "/reset-password")
   public Object resetPassword(@RequestBody @Valid BaseRequest baseRequest) {
      return userService.resetPassword(baseRequest);
   }

   @PatchMapping(value = "/reset-password")
   public Object completeResetPassword(@RequestBody @Valid ResetPasswordRequest request) {
      return userService.completeResetPassword(request);
   }

   @GetMapping(value = "/status")
   public Object test() {
      return Map.of("status", "success");
   }


}
