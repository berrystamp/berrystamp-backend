package com.berryinkstamp.berrybackendservice.quabbly.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecordData {

    private String value;

    private List<String> values;

    private String fieldId;
}
