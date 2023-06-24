package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.dtos.request.PaystackTransactionRequest;
import com.berryinkstamp.berrybackendservice.services.PaymentService;
import com.berryinkstamp.berrybackendservice.services.WebHookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class WebHookImpl implements WebHookService {
    private final PaymentService paymentService;
    @Override
    public void processWebHookTransactionPayment(PaystackTransactionRequest request) {
        log.info("New paystack webhook Event : {}", request.getEvent());

        if (Objects.equals("charge.success", request.getEvent())) {
            log.info("New paystack webhook 2 successful charge");
            paymentService.processPaymentTransaction(request.getData().getReference());
        }
    }
}
