package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.annotations.ValidPrinter;
import com.berryinkstamp.berrybackendservice.annotations.ValidUrl;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @ValidUrl
    private String frontImageUrl;
    @ValidUrl
    private String backImageUrl;
    private String description;
    @ValidPrinter
    private Long printerId;
    private BigDecimal amount = BigDecimal.ZERO;
    @JsonProperty("categories")
    private Set<String> category = new HashSet<>();
    private Set<String> tags = new HashSet<>();
}
