package com.berryinkstamp.berrybackendservice.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCustomizationDTo {

    @NotNull(message = "design id required")
    private Long designId;

    @NotNull(message = "designer id required")
    private Long designerId;
}
