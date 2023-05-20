package com.berryinkstamp.berrybackendservice.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UpdateDesignRequest {
    @NotBlank(message = "name is required!")
    private String name;
    private String frontImageUrl;
    private String backImageUrl;
    private String description;
    private Long printerId;
    private BigDecimal amount;
    private Set<String> category = new HashSet<>();
    private Set<String> tags = new HashSet<>();
}
