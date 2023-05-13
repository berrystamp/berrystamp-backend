package com.berryinkstamp.berrybackendservice.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentDetailDto implements Serializable {
    @NotBlank(message = "bank name is required")
    private String bankName;

    @NotBlank(message = "bank code is required")
    private String bankCode;

    private String accountName;

    @NotBlank(message = "account number is required")
    private String accountNumber;
}
