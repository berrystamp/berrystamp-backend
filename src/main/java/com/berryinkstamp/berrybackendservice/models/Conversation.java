package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.ConversationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "converstions")
@AllArgsConstructor
@NoArgsConstructor
public class Conversation extends AbstractAuditingEntity<Conversation> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conversationName;

    private ConversationType type;

    private boolean hasUnreadMessages;

    private int unreadMessageCount;

    private LocalDateTime lastMessageTimestamp;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Profile> participants;
}
