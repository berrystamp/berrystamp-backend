package com.berryinkstamp.berrybackendservice.utils;

public class Utils {
    public static String generateSlug(String input) {
        String slug = input.toLowerCase().replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
        return slug;
    }
}
