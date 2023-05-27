package com.berryinkstamp.berrybackendservice.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderDto {
    private Long orderRequestId;
    private String orderTitle;
    private String description;
    private BigDecimal pickUpAmount;
}
