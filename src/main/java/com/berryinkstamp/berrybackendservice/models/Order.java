package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.name;

@Entity
@Getter
@Setter
@Table(name="orders")
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
    private String transactionId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.REVIEW;

    //todo move this to order request richard
    //todo create order and test richard,
    @JsonIgnore
    @OneToOne
    private Profile designerOrPrinterProfile;

    //todo remove this it is already in order request
    @JsonIgnore
    @OneToOne
    private Profile customerProfile;

    @OneToOne
    private OrderRequest orderRequest;


    private void calculateTotalAmount(){
        totalAmount = printingAmount.add(designAmount).add(pickupAmount);
    }
}
