package com.berryinkstamp.berrybackendservice.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaystackInitializeTransactionResponse {

    public boolean status;
    public String message;
    public PaystackInitializeTransactionResponse.Data data;

    @Getter
    @Setter
    public static class Data {
        @JsonProperty("authorization_url")
        private String authorizationUrl;

        @JsonProperty
        private String reference;

        @JsonProperty("access_code")
        private String accessCode;

    }
}
