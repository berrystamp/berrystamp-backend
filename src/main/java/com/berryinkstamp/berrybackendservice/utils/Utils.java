package com.berryinkstamp.berrybackendservice.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class Utils {
    public static String generateSlug(String input) {
        String slug = input.toLowerCase().replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
        return slug + "-" + RandomStringUtils.randomAlphanumeric(3, 5);
    }
}
