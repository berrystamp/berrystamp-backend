package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.models.Transaction;
import com.berryinkstamp.berrybackendservice.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SecurityRequirement(name = "Bearer Authentication")
@RestController
@AllArgsConstructor
@WrapApiResponse
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(summary = "Fetch all logged in  user transactions", description = "Fetch all logged in user transactions")
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public Page<Transaction> fetchAllTransactions(Pageable pageable){
        return transactionService.fetchAllUserTransactions(pageable);
    }
}
