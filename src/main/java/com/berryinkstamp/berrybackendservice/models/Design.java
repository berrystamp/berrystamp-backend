package com.berryinkstamp.berrybackendservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
@Table(name = "design")
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

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(nullable = false)
    private Profile designer;

    private Long printer;

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
    @Column(name = "enabled", columnDefinition="BOOLEAN DEFAULT false")
    private boolean deleted;

    //todo add field boolean approved with false. and status : enum awaiting approval, approved declined
    @JsonProperty("tags")
    public List<String>tags() { return tag == null? null : List.of(tag.split(","));}

    @JsonProperty("categories")
    public List<String>categories() { return category == null? null : List.of(category.split(","));}
}
