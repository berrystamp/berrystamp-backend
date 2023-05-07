package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.models.MockImages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class NewDesignRequest {
    @NotBlank(message = "name is required!")
    private String name;
    private String frontImageUrl;
    private String backImageUrl;
    private String description;
    @NotNull
    private Long designerId;
    private Long printerId;
    private BigDecimal amount;
    private List<MockImages> mocks;
    private String category;
    private String tags;
}
