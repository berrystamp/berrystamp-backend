package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Order extends AbstractAuditingEntity<Order> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private BigDecimal printingAmount;
    private BigDecimal designAmount;
    private BigDecimal pickupAmount;
    private BigDecimal totalAmount;
    private Boolean paid = false;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.REVIEW;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Profile designerOrPrinterProfile;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Profile customerProfile;
    @OneToOne
    private OrderRequest orderRequest;


    private void calculateTotalAmount(){
        totalAmount = printingAmount.add(designAmount).add(pickupAmount);
    }
}
