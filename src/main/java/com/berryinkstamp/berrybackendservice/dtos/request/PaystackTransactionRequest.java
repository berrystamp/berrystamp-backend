package com.berryinkstamp.berrybackendservice.dtos.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaystackTransactionRequest  {
    private String event;
    private Data data;
}
