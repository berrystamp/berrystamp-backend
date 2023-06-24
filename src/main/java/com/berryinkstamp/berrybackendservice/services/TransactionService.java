package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.enums.PaymentProvider;
import com.berryinkstamp.berrybackendservice.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    void createTransaction(String reference, PaymentProvider paymentProvider);
    Page<Transaction>fetchAllTransactions(Pageable pageable);
    Page<Transaction> fetchAllUserTransactions(Pageable pageable);
    Page<Transaction>fetchAllTransactionsByProfileId(Long profileId, Pageable pageable);
}
