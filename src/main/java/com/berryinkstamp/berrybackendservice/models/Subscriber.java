package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "Subscribers")
public class Subscriber extends AbstractAuditingEntity<User> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String phoneNumber;
    private String email;
    @Enumerated(value = EnumType.STRING)
    private ProfileType profileType;

    @JsonIgnore
    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
}
