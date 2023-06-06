package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.enums.EmailType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SendEmailRequest {

    private String subject;

    private String body;

    private String templateId;

    @NotNull(message = "email type is required")
    private EmailType emailType;

    private List<String> recipients;

}
