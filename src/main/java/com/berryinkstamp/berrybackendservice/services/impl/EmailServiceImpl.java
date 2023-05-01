package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.dtos.request.EmailRequest;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailServiceFactory emailServiceFactory;

    @Value("${email.provider}")
    private String emailProvider;

    @Value("${registration.otp.templateId}")
    private String registrationOTPTemplateId;

    @Value("${password.otp.templateId}")
    private String forgetPasswordOTPTemplateId;

    @Value("${welcome.otp.templateId}")
    private String welcomeTemplateId;


    @Override
    public void sendRegistrationOTP(String otp, User user) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setBody(String.format("Hello %s, \nKindly use this %s to activate your account.", user.getName(), otp));
        emailRequest.setRecipients(user.getEmail());
        emailRequest.setSubject("Migra Account Activation");
        emailRequest.setTemplateId(registrationOTPTemplateId);
        emailRequest.setPlaceholders(Map.of("OtpCode", otp, "name", user.getName()));
        log.info("about to send email");
        emailServiceFactory.resolve(emailProvider).send(emailRequest);
        log.info("sent email");
    }

    @Override
    public void sendForgetPasswordOTP(String otp, User user) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setBody(String.format("Hello %s, \nKindly use this %s to reset your password.", user.getName(), otp));
        emailRequest.setRecipients(user.getEmail());
        emailRequest.setSubject("Password Reset");
        emailRequest.setTemplateId(forgetPasswordOTPTemplateId);
        emailRequest.setPlaceholders(Map.of("resetOtpCode", otp, "name", user.getName()));
        log.info("about to send email");
        emailServiceFactory.resolve(emailProvider).send(emailRequest);
        log.info("sent email");
    }

    @Override
    public void welcomeEmail(User user) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setBody(String.format("Hello %s, \nWelcome to Migra.\nFor more support please contact us..", user.getName()));
        emailRequest.setRecipients(user.getEmail());
        emailRequest.setSubject("Welcome");
        emailRequest.setTemplateId(forgetPasswordOTPTemplateId);
        emailRequest.setPlaceholders(Map.of("name", user.getName()));
        log.info("about to send email");
        emailServiceFactory.resolve(emailProvider).send(emailRequest);
        log.info("sent email");
    }

}
