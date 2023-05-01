package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.models.User;

public interface OTPService {
    void sendRegistrationOTP(User user);
    void sendForgetPasswordOTP(User user);
    boolean verifyOTP(User user, String code);
}
