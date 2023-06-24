package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.PaymentProvider;
import com.berryinkstamp.berrybackendservice.enums.TransactionStatus;
import jakarta.annotation.Generated;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Transaction extends AbstractAuditingEntity<Transaction> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String intTransactionRef;
    private String extTransactionRef;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @Enumerated(EnumType.STRING)
    private PaymentProvider paymentProvider;
    private BigDecimal amount;
    private Long userProfileId;
}
