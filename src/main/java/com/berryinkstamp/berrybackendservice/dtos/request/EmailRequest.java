package com.berryinkstamp.berrybackendservice.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class EmailRequest {
    private String subject;

    private String body;

    private String templateId;

    private String recipients;

    private Map<String, String> placeholders;

    private List<File> files;
}
