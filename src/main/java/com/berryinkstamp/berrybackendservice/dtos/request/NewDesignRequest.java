package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.models.MockImages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class NewDesignRequest {
    @NotBlank(message = "name is required!")
    private String name;
    private String frontImageUrl;
    private String backImageUrl;
    private String description;
    private Long printerId;
    private BigDecimal amount = BigDecimal.ZERO;
    private List<@Valid MockImagesDto> mocks;
    private Set<String> category = new HashSet<>();
    private Set<String> tags = new HashSet<>();
}
