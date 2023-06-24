package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.enums.OrderStatus;
import com.berryinkstamp.berrybackendservice.enums.PaymentProvider;
import com.berryinkstamp.berrybackendservice.enums.TransactionStatus;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.Order;
import com.berryinkstamp.berrybackendservice.models.Transaction;
import com.berryinkstamp.berrybackendservice.repositories.OrderRepository;
import com.berryinkstamp.berrybackendservice.repositories.TransactionRepository;
import com.berryinkstamp.berrybackendservice.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final TokenProvider tokenProvider;
    @Override
    public void createTransaction(String reference, PaymentProvider paymentProvider) {
        Order order = orderRepository.findOrderByTransactionRef(reference).orElse(null);
        if(order== null || order.getPaid()){
         return;
        }
        Transaction transaction = new Transaction();
        transaction.setExtTransactionRef(order.getTransactionRef());
        transaction.setIntTransactionRef(order.getTransactionRef());
        transaction.setAmount(order.getTotalAmount());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setPaymentProvider(paymentProvider);
        transaction.setUserProfileId(order.getOrderRequest().getCustomerProfile().getId());
        transaction.setOrderId(order.getId());

        transactionRepository.save(transaction);

        order.setPaid(true);
        order.setOrderStatus(OrderStatus.ACTIVE);
        orderRepository.save(order);
    }

    @Override
    public Page<Transaction> fetchAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @Override
    public Page<Transaction> fetchAllUserTransactions(Pageable pageable) {
        Long userId = tokenProvider.getCurrentUser().getCustomerProfile().getId();
        return transactionRepository.findAllByUserProfileId(userId, pageable);
    }

    @Override
    public Page<Transaction> fetchAllTransactionsByProfileId(Long profileId, Pageable pageable) {
        return transactionRepository.findAllByUserProfileId(profileId,pageable);
    }
}
