package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.enums.OrderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class OrderRequestCreation {
    private String orderTitle;
    private String orderDescription;
    private Set<Long> printRequestIds = new HashSet<>();
    private Long customDesignId;
    private BigDecimal printAmount;
//    private BigDecimal designAmount;
    private BigDecimal pickupAmount;
    private Long customerProfileId;
    private Long orderRequestId;
    @NotBlank(message = "Order type must be specified")
    private OrderType orderType;
}
