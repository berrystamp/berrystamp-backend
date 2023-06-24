package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.PaystackTransactionRequest;

public interface WebHookService {
    void processWebHookTransactionPayment(PaystackTransactionRequest request);
}
