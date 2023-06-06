package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.enums.RoleName;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddAdminRequest {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "email is required")
    private String email;

    private RoleName role = RoleName.ROLE_MEMBER;

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
