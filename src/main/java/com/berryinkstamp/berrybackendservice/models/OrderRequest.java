package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.OrderStatus;
import com.berryinkstamp.berrybackendservice.enums.OrderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Setter
@Getter
public class OrderRequest extends AbstractAuditingEntity<Order> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private PrintRequest printRequest;
    @OneToOne
    private Profile customerProfile;
//    @OneToOne
//    private Profile printerProfile;
    @OneToOne
    private CustomDesignRequest customDesignRequest;
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    private BigDecimal budgetAmount;
    private LocalDate dateOfDelivery;

}
