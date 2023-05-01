package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.CustomAuthenticationToken;
import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.BaseRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.LoginRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.RegistrationRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.ResetPasswordRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdatePasswordRequest;
import com.berryinkstamp.berrybackendservice.dtos.response.LoginResponse;
import com.berryinkstamp.berrybackendservice.dtos.response.RegistrationResponse;
import com.berryinkstamp.berrybackendservice.enums.AuthProvider;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.enums.RoleName;
import com.berryinkstamp.berrybackendservice.exceptions.BadRequestException;
import com.berryinkstamp.berrybackendservice.exceptions.ForbiddenException;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.exceptions.UnathorizedException;
import com.berryinkstamp.berrybackendservice.models.MailSetting;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.models.Role;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.repositories.MailSettingRepository;
import com.berryinkstamp.berrybackendservice.repositories.ProfileRepository;
import com.berryinkstamp.berrybackendservice.repositories.RoleRepository;
import com.berryinkstamp.berrybackendservice.repositories.UserRepository;
import com.berryinkstamp.berrybackendservice.services.EmailService;
import com.berryinkstamp.berrybackendservice.services.OTPService;
import com.berryinkstamp.berrybackendservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final OTPService otpService;
    private final EmailService emailService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSettingRepository mailSettingRepository;
    private final ProfileRepository profileRepository;
    private final RoleRepository authorityRepository;
    private final AuthenticationManager authenticationManager;


    @Override
    public RegistrationResponse registerUser(RegistrationRequest dto) {
        Optional<User> optionalUser = userRepository.findFirstByEmail(dto.getEmail());
        if (optionalUser.isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        if (dto.getProfile() == ProfileType.CUSTOMER) {
            validateCustomerRegistration(dto);
        }

        if (dto.getProfile() == ProfileType.PRINTER) {
            validatePrinterRegistration(dto);
        }

        if (dto.getProfile() == ProfileType.DESIGNER) {
            validateDesignerRegistration(dto);
        }

        User user = createUser(dto);
        otpService.sendRegistrationOTP(user);
        //todo push to audit
        return new RegistrationResponse(false);
    }


    @Override
    public Object resendCode(BaseRequest baseRequest) {
        User user = userRepository.findFirstByEmail(baseRequest.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        otpService.sendRegistrationOTP(user);
        return null;
    }

    @Override
    public LoginResponse activateAccount(String otp, BaseRequest baseRequest) {
        User user = userRepository.findFirstByEmail(baseRequest.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        boolean success = otpService.verifyOTP(user, otp);
        if (!success) {
            throw new BadRequestException("Invalid token");
        }

        user.setEnabled(true);
        user.setActivated(true);
        user = userRepository.save(user);

        CustomAuthenticationToken authenticationToken = new CustomAuthenticationToken(user.getEmail(), "", AuthProvider.activation);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);
        emailService.welcomeEmail(user);
        //todo push to audit
        return new LoginResponse(user, jwt);
    }

    @Override
    public Object resetPassword(BaseRequest baseRequest) {
        User user = userRepository.findFirstByEmail(baseRequest.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        otpService.sendForgetPasswordOTP(user);
        return null;
    }



    @Override
    public Object completeResetPassword(ResetPasswordRequest request) {
        User user = userRepository.findFirstByEmail(request.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        boolean success = otpService.verifyOTP(user, request.getOtp());
        if (!success) {
            throw new BadRequestException("Invalid otp");
        }
        String encodePassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodePassword);

        userRepository.save(user);

        //todo push to audit

        return null;
    }

    @Override
    public Object validateCode(String otp, BaseRequest request) {
        User user = userRepository.findFirstByEmail(request.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        boolean success = otpService.verifyOTP(user, otp);
        if (!success) {
            throw new BadRequestException("Invalid otp");
        }
        return null;
    }

    @Override
    public User updatePassword(UpdatePasswordRequest request) {

        User user = getCurrentUser();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        String encodePassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodePassword);

        user = userRepository.save(user);

        //todo push to audit

        return user;
    }

    @Override
    public LoginResponse login(LoginRequest dto) {

        User user = userRepository.findFirstByEmail(dto.getEmail()).orElseThrow(()->new NotFoundException("User not found"));

        if (!user.isActivated()) {
            throw new UnathorizedException("Email not verified");
        }

        if (!user.isEnabled()) {
            throw new ForbiddenException("Account suspended. Please contact support");
        }

        if (dto.getProfile() == ProfileType.PRINTER && user.getPrinterProfile() == null) {
            throw new ForbiddenException("You do not have a printer profile attached to this email");
        }

        if (dto.getProfile() == ProfileType.DESIGNER && user.getDesignerProfile() == null) {
            throw new ForbiddenException("You do not have a designer profile attached to this email");
        }

        CustomAuthenticationToken authenticationToken = new CustomAuthenticationToken(dto.getEmail(), dto.getPassword(), AuthProvider.local);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = dto.getRememberMe() != null && dto.getRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);

        user.setLastLoginDate(LocalDateTime.now());
        user = userRepository.save(user);

        //todo push to audit

        return new LoginResponse(user, jwt);

    }

    @Override
    public User getUser() {
        return getCurrentUser();
    }

    private void validateDesignerRegistration(RegistrationRequest dto) {
        if (dto.getBusinessName() == null) {
            throw new BadRequestException("Shop name is required");
        }

        if (dto.getBusinessName().trim().length() < 3) {
            throw new BadRequestException("Shop name too short, minimum character is 3");
        }

        if (profileRepository.existsByProfileTypeAndBusinessNameAllIgnoreCase(ProfileType.DESIGNER, dto.getBusinessName())) {
            throw new BadRequestException("Shop name already exists");
        }
    }

    private void validatePrinterRegistration(RegistrationRequest dto) {
        if (dto.getBusinessName() == null) {
            throw new BadRequestException("Business name is required");
        }

        if (dto.getBusinessName().trim().length() < 3) {
            throw new BadRequestException("Business name too short, minimum character is 3");
        }

        if (profileRepository.existsByProfileTypeAndBusinessNameAllIgnoreCase(ProfileType.PRINTER, dto.getBusinessName())) {
            throw new BadRequestException("Business name already exists");
        }
    }

    private void validateCustomerRegistration(RegistrationRequest dto) {
        if (dto.getUsername() == null ) {
            throw new BadRequestException("Username is required");
        }

        if (dto.getUsername().trim().length() < 3) {
            throw new BadRequestException("Username too short, minimum character is 3");
        }

        if (userRepository.existsByUsernameAllIgnoreCase(dto.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
    }

    private Set<Role> getRoles(ProfileType profile) {
        Set<Role> authorities = new HashSet<>();
        Role userAuthority = authorityRepository.findByName(RoleName.ROLE_CUSTOMER).orElseThrow(() ->new BadRequestException("Unable to complete Registration at the moment"));
        authorities.add(userAuthority);

        if (profile == ProfileType.DESIGNER) {
            Role designerAuthority = authorityRepository.findByName(RoleName.ROLE_DESIGNER).orElseThrow(() ->new BadRequestException("Unable to complete Registration at the moment"));
            authorities.add(designerAuthority);
        }

        if (profile == ProfileType.PRINTER) {
            Role printerAuthority = authorityRepository.findByName(RoleName.ROLE_PRINTER).orElseThrow(() ->new BadRequestException("Unable to complete Registration at the moment"));
            authorities.add(printerAuthority);
        }

        return authorities;
    }

    private User createUser(RegistrationRequest dto) {

        String userName = createUserName(dto);
        User user = new User();
        user.setUsername(userName);
        user.setEmail(dto.getEmail().trim());
        user.setName(dto.getName().trim());
        user.setEnabled(false);
        user.setRoles(getRoles(dto.getProfile()));
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setActivated(false);

        user =  userRepository.save(user);
        createMailSetting(user, dto);
        createProfile(dto, user);
        return user;
    }

    private String createUserName(RegistrationRequest dto) {
        if (dto.getUsername() == null) {
            return generateUserName(dto.getName());
        }

        return dto.getUsername();
    }

    private String generateUserName(String name) {
        name = name.replaceAll("\\s+", "");
        name = name + "-" +RandomStringUtils.randomAlphanumeric(3, 6);
        while (userRepository.existsByUsernameAllIgnoreCase(name)) {
            name = name + RandomStringUtils.randomAlphanumeric(3, 6);
        }
        return name;
    }

    private void createProfile(RegistrationRequest dto, User user) {
        if (dto.getProfile() == ProfileType.CUSTOMER) {
            return;
        }

        Profile profile = new Profile();
        profile.setBusinessName(dto.getBusinessName());
        profile.setUser(user);
        profile = profileRepository.save(profile);

        if (dto.getProfile() == ProfileType.DESIGNER) {
            user.setDesignerProfile(profile);
            return;
        }

        user.setPrinterProfile(profile);
    }

    private void createMailSetting(User user, RegistrationRequest dto) {
        MailSetting mailSetting = new MailSetting();
        mailSetting.setPromotionEmail(dto.isSendPromotionEmail());
        mailSetting.setSupportEmail(dto.isSendPromotionEmail());
        mailSetting.setOrderEmail(true);
        mailSetting.setOtherEmail(true);
        mailSetting.setNewsEmail(true);
        mailSetting.setUser(user);
        mailSetting = mailSettingRepository.save(mailSetting);
        user.setMailSetting(mailSetting);
    }


    private User getCurrentUser() {
        return userRepository.findFirstByEmail(tokenProvider.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
