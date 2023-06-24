package com.berryinkstamp.berrybackendservice.services;

import java.math.BigDecimal;

public interface PaymentService {
    String initializeTransaction(BigDecimal amount, String callbackUrl, String ref);
    void processPaymentTransaction(String reference);
}
