package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.models.Admin;
import com.berryinkstamp.berrybackendservice.models.User;

public interface EmailService {

    void sendRegistrationOTP(String otp, User user);
    void sendAdminResetOTP(String otp, Admin user);
    void welcomeEmail(User user);

    void sendForgetPasswordOTP(String otp, String email, String name);
}
