package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.annotations.ValidPassword;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest implements Serializable {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "Invalid email")
    private String email;

    private String username;

    private String businessName;

    @ValidPassword
    @NotBlank(message = "password is required")
    private String password;

    private boolean sendPromotionEmail;


}
