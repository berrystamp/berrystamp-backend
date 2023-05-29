package com.berryinkstamp.berrybackendservice.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
public class CustomDesignRequestDto {
    @NotNull(message = "designer profile id cannot be null")
    private Long designerProfileId;
    private Set<String> mockTypes = new HashSet<>();
    @NotBlank(message = "purpose is required")
    private String purpose;
    @NotBlank(message = "theme is required")
    private String theme;
    private LocalDate dateOfDelivery;
    private BigDecimal estimatedAmount;
}
