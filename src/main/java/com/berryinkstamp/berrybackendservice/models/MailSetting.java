package com.berryinkstamp.berrybackendservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "mail_settings")
@AllArgsConstructor
@NoArgsConstructor
public class MailSetting extends AbstractAuditingEntity<MailSetting> implements Serializable {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean supportEmail;
    private boolean orderEmail;
    private boolean newsEmail;
    private boolean otherEmail;
    private boolean promotionEmail;
    //todo : crud payment details in profile
    @JsonIgnore
    @OneToOne
    @JoinColumn(nullable = false)
    private User user;
}
