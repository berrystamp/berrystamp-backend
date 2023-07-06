package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.DesignStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "designs")
@AllArgsConstructor
@NoArgsConstructor
public class Design extends AbstractAuditingEntity<Design> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String imageUrlFront;
    private String imageUrlBack;
    private String description;
    private String slug;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Profile designer;

    @ManyToOne(fetch = FetchType.EAGER)
    private Profile printer;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "design")
    private Set<MockImages> mocks;

    @JsonIgnore
    private String tag;

    private BigDecimal amount;

    @ManyToOne
    @JsonIgnore
    private Collection collection;

    @JsonIgnore
    private String category;

    @JsonIgnore
    @Column(name = "deleted", columnDefinition="BOOLEAN DEFAULT false")
    private boolean deleted;


    @Column(name = "approved", columnDefinition="BOOLEAN DEFAULT false")
    private boolean approved;

    @Transient
    private boolean designIsLiked;

    @Enumerated(EnumType.STRING)
    private DesignStatus status = DesignStatus.AWAITING_APPROVAL;
    @JsonProperty("tags")
    public List<String>tags() { return tag == null? null : List.of(tag.split(","));}

    @JsonProperty("categories")
    public List<String>categories() { return category == null? null : List.of(category.split(","));}

}
