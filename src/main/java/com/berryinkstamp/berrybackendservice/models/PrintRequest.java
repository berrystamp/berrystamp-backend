package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.CustomDesignStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class PrintRequest extends AbstractAuditingEntity<PrintRequest> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long designId;
    private String designFrontImageUrl;
    private String designBackImageUrl;
    private BigDecimal designAmount;
    private String mockItemUrl;
    private String mockName;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Profile printerProfile;
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn
//    private Profile customerProfile;
    private String sourceOfItem;
    private Long quantity;
    private String colour;
    private String size;
}
