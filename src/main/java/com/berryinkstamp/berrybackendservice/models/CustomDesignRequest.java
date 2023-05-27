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

@Entity
@Getter
@Setter
@Table(name = "custom_design")
public class CustomDesignRequest extends AbstractAuditingEntity<Order> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    private String mockTypes;
    private String theme;
    private String purpose;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Profile designerProfile;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Profile customerProfile;
    private Long noOfReviews;
    private String imageUrlBack;
    private String imageUrlFront;
    @OneToMany(mappedBy = "custom_design",fetch = FetchType.EAGER)
    private Set<MockImages>mocks;
    @Column(length = 1000)
    private String reviews;
    @Enumerated(EnumType.STRING)
    private CustomDesignStatus customDesignStatus = CustomDesignStatus.UNDER_REVIEW;
    private Boolean isReferenceDesign = false;
    @JsonProperty("mock_types")
    public List<String> assignMockTypes (){
       return mockTypes == null? null : List.of(mockTypes.split(","));
    }

    public void setMockTypes(Set<String> mockList){
        mockTypes = mockList.toString();
    }

}
