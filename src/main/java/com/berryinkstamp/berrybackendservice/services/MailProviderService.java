package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.EmailRequest;
import com.berryinkstamp.berrybackendservice.enums.MailProvider;

public interface MailProviderService {
    void send(EmailRequest email);
    boolean canApply(MailProvider provider);
}
