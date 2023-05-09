package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.models.OTPMapper;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.repositories.OTPMapperRepository;
import com.berryinkstamp.berrybackendservice.services.EmailService;
import com.berryinkstamp.berrybackendservice.services.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

    private final OTPMapperRepository otpMapperRepository;

    private final EmailService emailService;

    @Value("${otp.line.span:10}")
    private Long otpLivedTime;


    @Override
    public void sendRegistrationOTP(User user) {
        String otp = RandomStringUtils.randomNumeric(6);
        log.info("registration otp {} for user {}", otp, user.getEmail());
        try {
            emailService.sendRegistrationOTP(otp, user);
        }catch (Exception e){
            e.printStackTrace();
        }
        saveOTP(user, otp);
    }

    @Override
    public void sendForgetPasswordOTP(User user) {
        String otp = RandomStringUtils.randomNumeric(6);
        log.info("forget password otp {} for user {}", otp, user.getEmail());
        try {
            emailService.sendForgetPasswordOTP(otp, user);
        }catch (Exception e){
            e.printStackTrace();
        }
        saveOTP(user, otp);
    }



    @Override
    public boolean verifyOTP(User user, String code) {
        Optional<OTPMapper> otpMapper = otpMapperRepository.findFirstByUserId(user.getId());
        return otpMapper.filter(mapper -> Objects.equals(mapper.getOtp(), code)).isPresent();
    }

    private void saveOTP(User user, String otp) {
        Optional<OTPMapper> otpMapper = otpMapperRepository.findFirstByUserId(user.getId());
        OTPMapper mapper = new OTPMapper(otp, user.getId());
        if (otpMapper.isPresent()) {
            mapper = otpMapper.get();
            mapper.setOtp(otp);
        }
        mapper.setCreatedAt(LocalDateTime.now().plusMinutes(otpLivedTime));
        otpMapperRepository.save(mapper);
    }
}
