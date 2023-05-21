package com.berryinkstamp.berrybackendservice.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Message implements Serializable {
    private Long fromProfileId;
    private Long toProfileId;
    private Long orderId;
    private String content;
    private boolean isOrderRequest;
}
