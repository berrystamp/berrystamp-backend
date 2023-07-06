package com.berryinkstamp.berrybackendservice.quabbly.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileInputDTO {
    @NotBlank(message = "id is required")
    private String id;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "url is required")
    private String url;

    @NotBlank(message = "type is required")
    private String type;

    private int size;

    @Override
    public String toString() {
        return "{ id:\"" + id + "\", name: \"" + name + "\", url:\"" + url + "\", type:\"" + type + "\", size:\"" + size + "\"}";
    }
}
