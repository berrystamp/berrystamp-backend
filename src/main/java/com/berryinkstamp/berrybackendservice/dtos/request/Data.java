package com.berryinkstamp.berrybackendservice.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class Data {

    private long id;

    private String domain;

    private String status;

    private String reference;

    @JsonProperty("refund_reference")
    private String refundReference;

    @JsonProperty("transaction_reference")
    private String transactionReference;

    private BigDecimal amount;
    private String message;
    private Date paid_at;
    private Date created_at;
    private String channel;
    private String currency;
    private Object metadata;
    private Object log;
    private String fees;
    private Object customer;
    private Object authorization;
    private Object plan;
    private Object split;
    private Object source;
    public Date paidAt;

}
