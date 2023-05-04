package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProfileRequest {
    @NotNull(message = "profile is required")
    private ProfileType profile;

    @NotBlank(message = "name is required")
    private String name;
}
