package com.berryinkstamp.berrybackendservice.quabbly.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class QuabblyRecordInputDTO {

    @NotNull
    private Set<Metadata> metadata;

    @Override
    public String toString() {
        return "{ metadata:" + metadata + '}';
    }
}
