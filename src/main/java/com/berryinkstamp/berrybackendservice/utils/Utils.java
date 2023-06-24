package com.berryinkstamp.berrybackendservice.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


public class Utils {
    public static String generateSlug(String input) {
        String slug = input.toLowerCase().replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
        return slug + "-" + RandomStringUtils.randomAlphanumeric(3, 5);
    }


            public static HttpHeaders getHeaders(String accessToken) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                return headers;
            }


}
