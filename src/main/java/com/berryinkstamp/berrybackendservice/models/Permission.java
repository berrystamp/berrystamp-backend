package com.berryinkstamp.berrybackendservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class Permission extends AbstractAuditingEntity<Permission> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean canManagerAdmin;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean canManagerUsers;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean canManagerDesigns;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean canManagerOrders;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean canManagerTransactions;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean canManagerPayments;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean canManagerTickets;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean canManagerBlogs;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean canManagerEmails;

    @OneToOne
    @JoinColumn(nullable = false)
    private Admin admin;


}
