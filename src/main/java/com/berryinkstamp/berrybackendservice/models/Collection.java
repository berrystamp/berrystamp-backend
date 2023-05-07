package com.berryinkstamp.berrybackendservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "collection")
@AllArgsConstructor
@NoArgsConstructor
public class Collection extends AbstractAuditingEntity<Collection> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String picture;
    private String slug;
    private String description;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "collection")
    private Set<Design> designs = new HashSet<>();
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    @JsonIgnore
    private Profile designerProfile;

}
