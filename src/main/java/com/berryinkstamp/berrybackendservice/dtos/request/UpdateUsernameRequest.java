package com.berryinkstamp.berrybackendservice.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UpdateUsernameRequest {
    @NotBlank(message = "name is required")
    private String name;
}
