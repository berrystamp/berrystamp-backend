package com.berryinkstamp.berrybackendservice.controllers.admin;

import com.berryinkstamp.berrybackendservice.annotations.WrapApiResponse;
import com.berryinkstamp.berrybackendservice.dtos.request.SendEmailRequest;
import com.berryinkstamp.berrybackendservice.dtos.response.Subscriber;
import com.berryinkstamp.berrybackendservice.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/email")
@WrapApiResponse
@RequiredArgsConstructor
public class AdminEmailController {

   private final AdminService adminService;


   @Operation(summary = "Add admin", description = "Add admin")
   @PostMapping
   public Object sendEmail(@Valid @RequestBody SendEmailRequest request) {
      return adminService.sendEmail(request);
   }

   @Operation(summary = "Get Subscribers", description = "Get Subscribers")
   @GetMapping("/subscribers")
   public List<Subscriber> getSubscribers() {
      return adminService.getSubscribers();
   }

}
