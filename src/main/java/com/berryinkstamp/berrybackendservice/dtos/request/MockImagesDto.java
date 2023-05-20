package com.berryinkstamp.berrybackendservice.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MockImagesDto implements Serializable {
    private Boolean limitedStatus;
    @NotBlank(message = "mock image url is required")
    private String imageUrl;
    private Long availableQty;
    @NotBlank(message = "mock image name is required")
    private String name;
}
