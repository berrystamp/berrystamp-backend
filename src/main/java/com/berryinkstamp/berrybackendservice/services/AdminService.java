package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.AddAdminRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.BaseRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.EditAdminRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.LoginRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.ResetPasswordRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.SendEmailRequest;
import com.berryinkstamp.berrybackendservice.dtos.response.LoginResponse;
import com.berryinkstamp.berrybackendservice.dtos.response.Subscriber;
import com.berryinkstamp.berrybackendservice.models.Admin;

import java.util.List;

public interface AdminService {
    Admin addAdmin(AddAdminRequest request);

    Object resendAccountActivationCode(BaseRequest request);

    Object deleteAdminUser(Long adminId);

    Admin editAdminUser(Long adminId, EditAdminRequest request);

    Object sendEmail(SendEmailRequest request);

    List<Subscriber> getSubscribers();

    LoginResponse login(LoginRequest loginRequest);

    Object resetPassword(BaseRequest baseRequest);

    Object completeResetPassword(ResetPasswordRequest request);
}
