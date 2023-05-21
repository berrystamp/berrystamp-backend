package com.berryinkstamp.berrybackendservice.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MarkMessageAsRead implements Serializable {
    private Long receiverProfileId;
    private Long messageId;
}
