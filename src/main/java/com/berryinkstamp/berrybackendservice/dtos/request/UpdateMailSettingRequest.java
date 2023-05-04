package com.berryinkstamp.berrybackendservice.dtos.request;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UpdateMailSettingRequest {
    private boolean supportEmail;
    private boolean orderEmail;
    private boolean newsEmail;
    private boolean otherEmail;
    private boolean promotionEmail;
}
