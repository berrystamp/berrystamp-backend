package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.annotations.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UpdatePasswordRequest {
    @NotBlank(message = "old password is required")
    private String oldPassword;

    @ValidPassword
    @NotBlank(message = "new password is required")
    private String newPassword;
}
