package com.berryinkstamp.berrybackendservice.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ReviewDto implements Serializable {
    @Size(min = 5, message = "comment length must be between 5 and 250", max = 250)
    private String comment;
    @Min(value = 1, message = "maximum star is 0")
    @Max(value = 5, message = "maximum star is 5")
    private int stars;
    private Long profileId;
}
