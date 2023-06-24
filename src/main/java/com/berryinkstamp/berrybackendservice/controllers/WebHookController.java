package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.dtos.request.PaystackTransactionRequest;
import com.berryinkstamp.berrybackendservice.services.PaymentService;
import com.berryinkstamp.berrybackendservice.services.WebHookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/webhook")
@Slf4j
@AllArgsConstructor
public class WebHookController {
   private final WebHookService webHookService;

    @PostMapping("/paystack")
    public ResponseEntity<?> paymentWebhookUrl(@RequestBody PaystackTransactionRequest request){
        log.info("New paystack webhook : {}", request);
        webHookService.processWebHookTransactionPayment(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
