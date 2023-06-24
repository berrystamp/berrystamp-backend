package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.RestTemplateConfig;
import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.PaystackInitializeTransactionRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.PaystackInitializeTransactionResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.PaystackTransactionRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.PaystackVerifyPaymentResponse;
import com.berryinkstamp.berrybackendservice.enums.PaymentProvider;
import com.berryinkstamp.berrybackendservice.exceptions.BadRequestException;
import com.berryinkstamp.berrybackendservice.models.Transaction;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.repositories.TransactionRepository;
import com.berryinkstamp.berrybackendservice.services.PaymentService;
import com.berryinkstamp.berrybackendservice.services.TransactionService;
import com.berryinkstamp.berrybackendservice.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class PaystackPaymentServiceImpl implements PaymentService {
    @Value("${paystack.secret.key]")
    private String payStackKey;
    private final TokenProvider tokenProvider;
    private final TransactionService transactionService;
    private final RestTemplateConfig restTemplate;

    @Override
    public String initializeTransaction(BigDecimal amount, String callbackUrl, String ref) {
        User customer = tokenProvider.getCurrentUser();
        PaystackInitializeTransactionRequest request = new PaystackInitializeTransactionRequest(
                String.valueOf(amount.multiply(new BigDecimal(100))), customer.getEmail(), ref, callbackUrl);
        return initializePaystackTransaction(request);
    }

    @Override
    public void processPaymentTransaction(String reference) {
        PaystackVerifyPaymentResponse response = verifyPaystackTransaction(reference);
        if (Objects.isNull(response) || Objects.isNull(response.getData())) {
            return;
        }
        transactionService.createTransaction(reference, PaymentProvider.PAYSTACK);
    }


    private String initializePaystackTransaction(PaystackInitializeTransactionRequest request) {
        try {

            HttpEntity<?>entity = new HttpEntity<>(request, Utils.getHeaders(payStackKey));
            String url = "https://api.paystack.co/transaction/initialize";

            ResponseEntity<PaystackInitializeTransactionResponse> responseEntity = restTemplate.getRestTemplate().exchange(url, HttpMethod.POST, entity, PaystackInitializeTransactionResponse.class);
            return Objects.requireNonNull(responseEntity.getBody()).getData().getAuthorizationUrl();
        } catch (HttpClientErrorException ex){
            log.error("An error occurred while initializing payment : " );
            throw new BadRequestException(ex.getMessage());
        } catch (Exception ex){
            log.error("An error occurred while initializing payment : " );
            throw new BadRequestException("An error occurred while initializing payment : ");
        }
    }

    @SneakyThrows
    private PaystackVerifyPaymentResponse verifyPaystackTransaction(String reference) {
        try {
            HttpEntity<?> entity = new HttpEntity<>(Utils.getHeaders(payStackKey));
            String url = String.format("%s/%s", "https://api.paystack.co/transaction/verify", reference);
            log.info("url : {}", url);

            ResponseEntity<PaystackVerifyPaymentResponse> responseEntity = restTemplate.getRestTemplate().exchange(url, HttpMethod.GET, entity, PaystackVerifyPaymentResponse.class);
            PaystackVerifyPaymentResponse response = responseEntity.getBody();
            if (Objects.nonNull(response)) {
                log.info("response message : {}", new ObjectMapper().writeValueAsString(response));
            }
            return response;
        }catch (Exception ex){
//            log.info("An error occurred while verifying payment : " + PaystackUtil.class.getName(), ex);
            return null;
        }
    }


}
