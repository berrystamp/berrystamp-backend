package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.models.Admin;
import com.berryinkstamp.berrybackendservice.models.User;

public interface OTPService {
    void sendRegistrationOTP(User user);
    void sendAdminResetOTP(Admin user);
    void sendForgetPasswordOTP(String email, String name);
    boolean verifyOTP(String email, String code);
}
