package com.berryinkstamp.berrybackendservice.quabbly.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Metadata {

    private Object value;

    private Set<FileInputDTO> file;

    private String fieldId;

    private boolean field;

    public Metadata(String fieldId, Set<FileInputDTO> file) {
        this.file = file;
        this.fieldId = fieldId;
    }

    public Metadata(Object value, String fieldId) {
        this.value = value;
        this.fieldId = fieldId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        return Objects.equals(fieldId, metadata.fieldId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldId);
    }

    @Override
    public String toString() {
        if (Objects.nonNull(value))
            return "{ value:\"" + value + "\", fieldId:\"" + fieldId  + "\"}";

        if (Objects.nonNull(file))
            return "{ file:" + file + ", fieldId:\"" + fieldId  + "\"}";

        return "{ value:" + value + ", fieldId:\"" + fieldId  + "\"}";
    }


}
