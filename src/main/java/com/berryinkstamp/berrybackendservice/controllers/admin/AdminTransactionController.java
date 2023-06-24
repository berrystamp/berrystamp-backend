package com.berryinkstamp.berrybackendservice.controllers.admin;


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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer Authentication")
@AllArgsConstructor
@RestController
@WrapApiResponse
@RequestMapping("/api/v1/admin/transactions")
public class AdminTransactionController {
    private final TransactionService transactionService;

    @Operation(summary = "Fetch all transactions", description = "Fetch All transactions")
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<Transaction>fetchAllTransactions(Pageable pageable){
        return transactionService.fetchAllTransactions(pageable);
    }

    @Operation(summary = "Fetch all transactions for a user", description = "Fetch All transactions for a user")
    @GetMapping("/{profileId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<Transaction>fetchAllTransactions(@PathVariable Long profileId, Pageable pageable){
        return transactionService.fetchAllTransactionsByProfileId(profileId, pageable);
    }
}
