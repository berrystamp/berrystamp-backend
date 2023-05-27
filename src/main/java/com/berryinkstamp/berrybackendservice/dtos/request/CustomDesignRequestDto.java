package com.berryinkstamp.berrybackendservice.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
public class CustomDesignRequestDto {
    @NotNull(message = "designer profile id cannot be null")
    private Long designerProfileId;
    private Set<String> mockTypes = new HashSet<>();
    private String purpose;
    private String theme;
    private String imageUrlBack;
    private String imageUrlFront;
    private LocalDate dateOfDelivery;
}
