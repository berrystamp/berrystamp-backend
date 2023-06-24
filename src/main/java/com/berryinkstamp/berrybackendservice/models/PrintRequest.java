package com.berryinkstamp.berrybackendservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

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
    private String sourceOfItem;
    private Long quantity;
    private String colour;
    private String size;
}
