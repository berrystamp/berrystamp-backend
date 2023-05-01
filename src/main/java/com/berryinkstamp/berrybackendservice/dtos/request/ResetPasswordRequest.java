package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.annotations.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ResetPasswordRequest {

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "otp is required")
    private String otp;

    @ValidPassword
    @NotBlank(message = "new password is required")
    private String password;

}
