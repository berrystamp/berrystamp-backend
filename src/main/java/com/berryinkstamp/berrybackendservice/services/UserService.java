package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.BaseRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.LoginRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.RegistrationRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.ResetPasswordRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdatePasswordRequest;
import com.berryinkstamp.berrybackendservice.dtos.response.LoginResponse;
import com.berryinkstamp.berrybackendservice.dtos.response.RegistrationResponse;
import com.berryinkstamp.berrybackendservice.models.User;

public interface UserService {
    RegistrationResponse registerUser(RegistrationRequest dto);

    Object resendCode(BaseRequest baseRequest);

    LoginResponse activateAccount(String otp, BaseRequest baseRequest);

    Object resetPassword(BaseRequest baseRequest);

    Object completeResetPassword(ResetPasswordRequest request);

    LoginResponse login(LoginRequest dto);

    User getUser();

    User updatePassword(UpdatePasswordRequest updatePinRequest);

    Object validateCode(String otp, BaseRequest baseRequest);
}
