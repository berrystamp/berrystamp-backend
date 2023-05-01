package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.enums.MailProvider;
import com.berryinkstamp.berrybackendservice.services.MailProviderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmailServiceFactory {
    private List<MailProviderService> mailProviders;

    public MailProviderService resolve(String mailProvider) {
        return mailProviders.stream().filter(m -> m.canApply(MailProvider.getByAlias(mailProvider)))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Unable to resolve payment service"));
    }
}
