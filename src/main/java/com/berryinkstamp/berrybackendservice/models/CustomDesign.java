package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.CustomDesignStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

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

    @OneToMany(mappedBy = "customDesign", fetch = FetchType.EAGER)
    private Set<CustomDesignReview> reviews;

    @Enumerated(EnumType.STRING)
    private CustomDesignStatus customDesignStatus = CustomDesignStatus.UNDER_REVIEW;

    private Long orderId;

    private Boolean isCompleted = false;



}
