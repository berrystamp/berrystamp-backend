package com.berryinkstamp.berrybackendservice.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MockImagesDto implements Serializable {
    private Boolean limitedStatus;
    private String imageUrl;
    private Long availableQty;
}
