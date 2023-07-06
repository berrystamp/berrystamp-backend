package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.models.Admin;
import com.berryinkstamp.berrybackendservice.models.User;

public interface OTPService {
    void sendRegistrationOTP(User user);
    void sendAdminResetOTP(Admin user);
    void sendForgetPasswordOTP(User user);
    boolean verifyOTP(String email, String code);
    void resendOTP(User user);
}
