package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.CustomDesignStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "custom_design")
public class CustomDesign extends AbstractAuditingEntity<CustomDesign> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Profile designerProfile;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Profile customerProfile;

    private Long noOfReviews;

    private String imageUrlBack;

    private String imageUrlFront;

    @OneToMany
    private Set<MockImages>mocks;

    @OneToMany
    private Set<Review> reviews; //todo should be a model

    @Enumerated(EnumType.STRING)
    private CustomDesignStatus customDesignStatus = CustomDesignStatus.UNDER_REVIEW;

    private Long orderId;

    private Boolean isCompleted = false;



}
