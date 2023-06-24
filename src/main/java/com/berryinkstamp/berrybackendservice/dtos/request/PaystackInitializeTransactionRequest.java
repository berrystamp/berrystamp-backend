package com.berryinkstamp.berrybackendservice.dtos.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaystackInitializeTransactionRequest {
    private List<String> channels = List.of("card");
    private String amount;
    private String email;
    private String currency = "NGN";
    private String reference;
    private String callback;

    public PaystackInitializeTransactionRequest(String amount, String email, String reference, String callback) {
        this.amount = amount;
        this.email = email;
        this.reference = reference;
        this.channels = List.of("card", "bank", "ussd", "qr", "mobile_money", "bank_transfer");
        this.callback = callback;
    }
}