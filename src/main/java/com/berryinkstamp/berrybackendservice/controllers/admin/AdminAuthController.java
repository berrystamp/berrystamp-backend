package com.berryinkstamp.berrybackendservice.controllers.admin;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.BaseRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.LoginRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.ResetPasswordRequest;
import com.berryinkstamp.berrybackendservice.dtos.response.LoginResponse;
import com.berryinkstamp.berrybackendservice.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/admin")
@WrapApiResponse
@RequiredArgsConstructor
public class AdminAuthController {

   private final AdminService adminService;


   @Operation(summary = "Admin Login")
   @PostMapping("/login")
   public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
      return adminService.login(loginRequest);
   }


   @Operation(summary = "Forget Password - Send OTP", description = "Forget Password - Send OTP")
   @PostMapping(value = "/reset-password")
   public Object resetPassword(@RequestBody @Valid BaseRequest baseRequest) {
      return adminService.resetPassword(baseRequest);
   }

   @Operation(summary = "Forget Password - Reset Password", description = "Forget Password - Reset Password")
   @PatchMapping(value = "/reset-password")
   public Object completeResetPassword(@RequestBody @Valid ResetPasswordRequest request) {
      return adminService.completeResetPassword(request);
   }


}
