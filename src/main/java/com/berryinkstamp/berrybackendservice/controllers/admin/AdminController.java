package com.berryinkstamp.berrybackendservice.controllers.admin;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.AddAdminRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.BaseRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.EditAdminRequest;
import com.berryinkstamp.berrybackendservice.models.Admin;
import com.berryinkstamp.berrybackendservice.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
@WrapApiResponse
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @Operation(summary = "Add admin", description = "Add admin")
    @PostMapping
    public Admin addAdmin(@Valid @RequestBody AddAdminRequest request) {
        return adminService.addAdmin(request);
    }

    @Operation(summary = "Resend Account Activation Code", description = "Resend Account Activation Code")
    @PostMapping("/resend-code")
    public Object resendAccountActivationCode(@RequestBody @Valid BaseRequest request) {
        return adminService.resendAccountActivationCode(request);
    }

    @Operation(summary = "Delete Admin User", description = "Delete Admin User")
    @DeleteMapping("/{adminId}")
    public Object deleteAdminUser(@PathVariable Long adminId) {
        return adminService.deleteAdminUser(adminId);
    }

    @Operation(summary = "Edit Admin User", description = "Edit Admin User")
    @PutMapping("/{adminId}")
    public Admin editAdminUser(@PathVariable Long adminId, @Valid @RequestBody EditAdminRequest request) {
        return adminService.editAdminUser(adminId, request);
    }
}
