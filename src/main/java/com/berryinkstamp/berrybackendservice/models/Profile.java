package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
public class Profile extends AbstractAuditingEntity<Profile> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProfileType profileType;

    private String businessName;
    private String profilePic;
    private String coverPic;
    private String bio;
    private int total1Star;
    private int total2Star;
    private int total3Star;
    private int total4Star;
    private int total5Star;
    private int avgStars;

    @JsonIgnore
    private String gallery;

    @JsonIgnore
    private String categories;

    @OneToOne(fetch = FetchType.EAGER)
    private PaymentDetail paymentDetail;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(nullable = false)
    private User user;

    @JsonProperty("galleries")
    public List<String> getGalleryOutput(){
        return gallery == null ? null : List.of(gallery.split(","));
    }

    @JsonProperty("categories")
    public List<String> getCategoryOutput(){
        return categories == null ? null : List.of(categories.split(","));
    }
}
