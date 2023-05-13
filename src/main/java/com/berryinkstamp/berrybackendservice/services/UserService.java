package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.AddProfileRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.BaseRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.LoginRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.PaymentDetailDto;
import com.berryinkstamp.berrybackendservice.dtos.request.RegistrationRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.ResetPasswordRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateMailSettingRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdatePasswordRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateUserRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateUsernameRequest;
import com.berryinkstamp.berrybackendservice.dtos.response.LoginResponse;
import com.berryinkstamp.berrybackendservice.dtos.response.RegistrationResponse;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Address;
import com.berryinkstamp.berrybackendservice.models.MailSetting;
import com.berryinkstamp.berrybackendservice.models.PaymentDetail;
import com.berryinkstamp.berrybackendservice.models.User;

public interface UserService {
    RegistrationResponse registerUser(RegistrationRequest dto, ProfileType profileType);

    Object resendCode(BaseRequest baseRequest);

    LoginResponse activateAccount(String otp, BaseRequest baseRequest);

    Object resetPassword(BaseRequest baseRequest);

    Object completeResetPassword(ResetPasswordRequest request);

    LoginResponse login(LoginRequest dto, ProfileType profileType);

    User getUser();

    User updatePassword(UpdatePasswordRequest updatePinRequest);

    Object validateCode(String otp, BaseRequest baseRequest);

    User updateUsername(UpdateUsernameRequest request);

    User updateUser(UpdateUserRequest request);

    MailSetting updateMailSetting(UpdateMailSettingRequest request);

    PaymentDetail updatePaymentDetail(PaymentDetailDto request);

    User addProfile(AddProfileRequest request);

    Address getAddress();

    MailSetting getMailSetting();

    PaymentDetail getPaymentDetails();
}
