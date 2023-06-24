package com.berryinkstamp.berrybackendservice.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPaymentDto {
    @NotBlank(message = "Callback is required")
    private String callback;
    @NotNull(message = "Order Id is required")
    private Long orderId;
}
