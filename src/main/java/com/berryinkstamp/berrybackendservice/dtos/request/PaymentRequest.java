package com.berryinkstamp.berrybackendservice.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentRequest {

    private Long planId;

    @NotBlank(message = "first name is required")
    private String firstName;

    @NotBlank(message = "last name is required")
    private String lastName;

    private String phone;

    @NotBlank(message = "email is required")
    private String email;

    @NotNull(message = "currency is required")
    private String currency;

    private Object cardInfo;

}
