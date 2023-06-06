package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.enums.RoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditAdminRequest {

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "role is required")
    private RoleName role;

    private boolean canManagerAdmin;

    private boolean canManagerUsers;

    private boolean canManagerDesigns;

    private boolean canManagerOrders;

    private boolean canManagerTransactions;

    private boolean canManagerPayments;

    private boolean canManagerTickets;

    private boolean canManagerBlogs;

    private boolean canManagerEmails;

}
