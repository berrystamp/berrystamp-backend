package com.berryinkstamp.berrybackendservice.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PrintRequestDto {
    private Long printerId;
    private Long designId;
    private String  colour;
    private Long quantity;
    private String size;
    private String sourceOfItem;
    private Long mockItemId;
    //todo validate amount is not negative.
    private BigDecimal estimatedAmount = BigDecimal.ZERO;
    private LocalDate dateOfDelivery;
}
