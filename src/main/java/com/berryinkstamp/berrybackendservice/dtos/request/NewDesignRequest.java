package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.annotations.ValidPrinter;
import com.berryinkstamp.berrybackendservice.annotations.ValidUrl;
import com.berryinkstamp.berrybackendservice.models.MockImages;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewDesignRequest {
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
    @NotEmpty(message =  "mocks are required")
    private List<@Valid MockImagesDto> mocks = new ArrayList<>();
    @JsonProperty("categories")
    private Set<String> category = new HashSet<>();
    private Set<String> tags = new HashSet<>();
}
