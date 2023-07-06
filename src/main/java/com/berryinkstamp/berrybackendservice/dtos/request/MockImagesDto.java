package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.annotations.ValidUrl;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MockImagesDto implements Serializable {

    private boolean limitedStatus;

    @NotBlank(message = "mock image url is required")
    @ValidUrl
    private String imageUrl;

    @Min(value = 0)
    private long availableQty;

    @NotBlank(message = "mock image name is required")
    private String name;
}
