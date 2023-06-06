package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.DisputeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "disputes")
@AllArgsConstructor
@NoArgsConstructor
public class Dispute extends AbstractAuditingEntity<Dispute> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private DisputeType disputeType;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean ticketCreated;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Profile customerProfile;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;

}
