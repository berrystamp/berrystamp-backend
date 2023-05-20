package com.berryinkstamp.berrybackendservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
public class Review extends AbstractAuditingEntity<Review> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private int stars;

    private int service;

    private int deliveryTime;

    private int recommendation;
    private int communication;

    @ManyToOne(fetch = FetchType.EAGER)
    private Profile ratedUser;

    @ManyToOne(fetch = FetchType.EAGER)
    private Profile ratingUser;

}
