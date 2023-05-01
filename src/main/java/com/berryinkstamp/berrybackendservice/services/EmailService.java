package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.models.User;

public interface EmailService {

    void sendRegistrationOTP(String otp, User user);
    void sendForgetPasswordOTP(String otp, User user);
    void welcomeEmail(User user);

}
