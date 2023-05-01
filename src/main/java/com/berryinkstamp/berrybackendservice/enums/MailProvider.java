package com.berryinkstamp.berrybackendservice.enums;


import java.util.HashMap;
import java.util.Map;

public enum MailProvider {
    SENDGRID("sendgrid");

    private final String alias;

    private static final Map<String, MailProvider> map;

    static {
        map = new HashMap<>();
        for (MailProvider mailProvider : MailProvider.values()) {
            map.put(mailProvider.alias, mailProvider);
        }
    }

    MailProvider(String alias) {
        this.alias = alias;
    }

    public static MailProvider getByAlias(String alias) {
        return map.get(alias);
    }

}
