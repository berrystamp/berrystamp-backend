package com.berryinkstamp.berrybackendservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "design")
@AllArgsConstructor
@NoArgsConstructor
public class Design extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String imageUrlFront;
    private String imageUrlBack;
    private String description;
    private String slug;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(nullable = false)
    private Profile designer;
    private Long printer;
    @OneToMany(fetch = FetchType.EAGER)
    private List<MockImages> mocks;
    @JsonIgnore
    private String tag;
    private BigDecimal amount;
    @ManyToOne
    @JsonIgnore
    private Collection collection;
    @JsonIgnore
    private String category;
    @JsonProperty("tags")
    public List<String>tags() { return tag == null? null : List.of(tag.split(","));}
    @JsonProperty("categories")
    public List<String>categories() { return category == null? null : List.of(category.split(","));}
}
