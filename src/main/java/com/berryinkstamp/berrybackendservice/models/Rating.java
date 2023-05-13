package com.berryinkstamp.berrybackendservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "rating")
@AllArgsConstructor
@NoArgsConstructor
public class Rating extends AbstractAuditingEntity<Rating> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int total1Star;
    private int total2Star;
    private int total3Star;
    private int total4Star;
    private int total5Star;
    private double avgStars;

    @OneToOne
    private Profile profile;

    public void updateStarCount(int stars) {
        if (stars == 1) {
            total1Star += 1;
        }

        if (stars == 2) {
            total2Star += 2;
        }

        if (stars == 3) {
            total3Star += 3;
        }

        if (stars == 4) {
            total4Star += 4;
        }

        if (stars == 5) {
            total5Star += 5;
        }

        calculateAvgStar();

    }

    private void calculateAvgStar() {
        avgStars = (total1Star + (2 * total2Star) + (3 * total3Star) + (4 * total4Star) + (5 * total5Star)) / getTotalRatingCount();
    }

    private double getTotalRatingCount() {
        return total1Star + total2Star + total3Star + total4Star + total5Star;
    }
}
