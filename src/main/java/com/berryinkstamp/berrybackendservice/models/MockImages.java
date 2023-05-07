package com.berryinkstamp.berrybackendservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "mock_images")
@AllArgsConstructor
@NoArgsConstructor
public class MockImages extends AbstractAuditingEntity<MockImages>implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean limitedStatus;
    private String imageUrl;
    @Column(name = "available_qty")
    private Long availableQty;
}
