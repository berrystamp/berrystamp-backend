package com.berryinkstamp.berrybackendservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name = "messages")
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends AbstractAuditingEntity<ChatMessage> implements Serializable {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String content;

    @OneToOne
    private Profile sender;

    @OneToOne
    private Profile receiver;

    private boolean read;


    @JsonIgnore
    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean isOrderRequest;

    private String messageIdentifier;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Conversation conversation;
}
