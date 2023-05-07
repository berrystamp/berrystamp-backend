package com.berryinkstamp.berrybackendservice.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCollectionRequest {
    @NotBlank(message = "name is required!")
    private String name;
    private String description;
    private String picture;
}
