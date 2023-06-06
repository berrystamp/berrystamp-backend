package com.berryinkstamp.berrybackendservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "custom_design_request")
public class CustomDesignRequest extends AbstractAuditingEntity<CustomDesign> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    private  String mockTypes;
    private  String theme;
    private  String purpose;
    private  Long designerProfileId; //todo remove this because it is already represented in orderRequest (designerOrPrinterProfile) richard
    private String imageUrlBack;
    private String imageUrlFront;
    private Long designId;
    private  Boolean isReferenceDesign;

    @JsonProperty("mockTypes")
    public Set<String> converMockTypeToSet() {
        return mockTypes == null ? new HashSet<>() : Set.of(mockTypes.split(","));
    }
}
