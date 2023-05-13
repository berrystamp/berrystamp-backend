package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
public class UpdateProfileRequest {

    @NotBlank(message = "name is required")
    private String name;

    private String profilePic;

    private String coverPic;

    private String bio;

    private List<String> categories = new ArrayList<>();
}
