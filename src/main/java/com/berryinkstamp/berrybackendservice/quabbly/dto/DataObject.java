package com.berryinkstamp.berrybackendservice.quabbly.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataObject {

    private Record record;

    private Record updateRecord;

    @JsonProperty("RecordById")
    private Record recordById;

    private String deleteRecord;

    private List<Record> boardRecords;
}
