package com.berryinkstamp.berrybackendservice.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AddSubscriberRequest {
    private String username;
    private String phoneNumber;
    private String email;
}
