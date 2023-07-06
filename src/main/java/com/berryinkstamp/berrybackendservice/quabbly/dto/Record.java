package com.berryinkstamp.berrybackendservice.quabbly.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Record {

    private String id;

    private Date createdTime;

    private Date lastUpdatedTime;

    private List<RecordData> data;

}
