package com.berryinkstamp.berrybackendservice.dtos.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaystackVerifyPaymentResponse {
    private boolean status;
    private String message;
    private Data data;
}
