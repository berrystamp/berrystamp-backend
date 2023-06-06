package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.cache.CountryCache;
import com.berryinkstamp.berrybackendservice.configs.security.CustomAuthenticationToken;
import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
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
import com.berryinkstamp.berrybackendservice.enums.AuthProvider;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.enums.RoleName;
import com.berryinkstamp.berrybackendservice.exceptions.BadRequestException;
import com.berryinkstamp.berrybackendservice.exceptions.ForbiddenException;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.exceptions.UnathorizedException;
import com.berryinkstamp.berrybackendservice.models.Address;
import com.berryinkstamp.berrybackendservice.models.MailSetting;
import com.berryinkstamp.berrybackendservice.models.PaymentDetail;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.models.Rating;
import com.berryinkstamp.berrybackendservice.models.Role;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.repositories.AddressRepository;
import com.berryinkstamp.berrybackendservice.repositories.MailSettingRepository;
import com.berryinkstamp.berrybackendservice.repositories.PaymentDetailRepository;
import com.berryinkstamp.berrybackendservice.repositories.ProfileRepository;
import com.berryinkstamp.berrybackendservice.repositories.RatingRepository;
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
import java.util.Map;
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
    private final RatingRepository ratingRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final MailSettingRepository mailSettingRepository;
    private final ProfileRepository profileRepository;
    private final RoleRepository authorityRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final AuthenticationManager authenticationManager;


    @Override
    public RegistrationResponse registerUser(RegistrationRequest dto, ProfileType profileType) {
        Optional<User> optionalUser = userRepository.findFirstByEmail(dto.getEmail());
        if (optionalUser.isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        if (profileType == ProfileType.CUSTOMER) {
            validateCustomerRegistration(dto);
        }

        if (profileType == ProfileType.PRINTER) {
            validatePrinterRegistration(dto);
        }

        if (profileType == ProfileType.DESIGNER) {
            validateDesignerRegistration(dto);
        }

        User user = createUser(dto, profileType);
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
        boolean success = otpService.verifyOTP(user.getEmail(), otp);
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
        otpService.sendForgetPasswordOTP(user.getEmail(), user.getName());
        return null;
    }

    @Override
    public Object completeResetPassword(ResetPasswordRequest request) {
        User user = userRepository.findFirstByEmail(request.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        boolean success = otpService.verifyOTP(user.getEmail(), request.getOtp());
        if (!success) {
            throw new BadRequestException("Invalid otp");
        }
        String encodePassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodePassword);

        userRepository.save(user);

        //todo push to audit

        return Map.of();
    }

    @Override
    public Object validateCode(String otp, BaseRequest request) {
        User user = userRepository.findFirstByEmail(request.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        boolean success = otpService.verifyOTP(user.getEmail(), otp);
        if (!success) {
            throw new BadRequestException("Invalid otp");
        }
        return Map.of();
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
    public User updateUsername(UpdateUsernameRequest request) {
        User user = getCurrentUser();
        Profile customerProfile = getCurrentUser().getCustomerProfile();

        if (profileRepository.existsByProfileTypeAndNameAllIgnoreCase(ProfileType.CUSTOMER, request.getName())) {
            throw new BadRequestException("Username already exists");
        }

        customerProfile.setName(request.getName());

        customerProfile = profileRepository.save(customerProfile);

        user.setCustomerProfile(customerProfile);

        //todo push to audit

        return user;
    }

    @Override
    public User updateUser(UpdateUserRequest request) {

        String country = getCountry(request.getCountry());
        User user = getCurrentUser();
        user.setGender(request.getGender());
        user.setName(request.getName());
        user = userRepository.save(user);

        Profile customerProfile = getCurrentUser().getCustomerProfile();
        customerProfile.setProfilePic(request.getProfilePicture());
        customerProfile = profileRepository.save(customerProfile);

        user.setCustomerProfile(customerProfile);
        updateAddress(request, user, country);
        return user;
    }

    private String getCountry(String countryName) {
        if (countryName == null) {
            return null;
        }
        String country = CountryCache.getInstance().get(countryName.toUpperCase().trim());
        if (country == null) {
            throw new BadRequestException("invalid country");
        }

        return country;

    }

    @Override
    public MailSetting updateMailSetting(UpdateMailSettingRequest request) {
        MailSetting mailSetting = mailSettingRepository.findByUser(tokenProvider.getCurrentUser()).orElse(new MailSetting());
        mailSetting.setPromotionEmail(request.isPromotionEmail());
        mailSetting.setSupportEmail(request.isSupportEmail());
        mailSetting.setOrderEmail(request.isOrderEmail());
        mailSetting.setOtherEmail(request.isOtherEmail());
        mailSetting.setNewsEmail(request.isNewsEmail());
        mailSetting.setUser(tokenProvider.getCurrentUser());
        mailSetting = mailSettingRepository.save(mailSetting);
        return mailSetting;
    }

    @Override
    public PaymentDetail updatePaymentDetail(PaymentDetailDto request) {
        //todo validate account details
        PaymentDetail paymentDetail = paymentDetailRepository.findByUser(tokenProvider.getCurrentUser()).orElse(new PaymentDetail());
        paymentDetail.setAccountName(request.getAccountName());
        paymentDetail.setAccountNumber(request.getAccountNumber());
        paymentDetail.setBankName(request.getBankName());
        paymentDetail.setBankCode(request.getBankCode());
        paymentDetail.setUser(tokenProvider.getCurrentUser());
        paymentDetail = paymentDetailRepository.save(paymentDetail);
        return paymentDetail;
    }

    @Override
    public User addProfile(AddProfileRequest request) {
        User user = getCurrentUser();

        if (request.getProfile() == ProfileType.PRINTER && user.getPrinterProfile() != null) {
            throw new BadRequestException("printer profile already exists");
        }

        if (request.getProfile() == ProfileType.DESIGNER && user.getDesignerProfile() != null) {
            throw new BadRequestException("designer profile already exists");
        }

        if (request.getProfile() == ProfileType.CUSTOMER ) {
            return user;
        }

        createProfile(request.getName(), request.getProfile(), user);

        return userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest dto, ProfileType profileType) {

        User user = userRepository.findFirstByEmail(dto.getEmail()).orElseThrow(()->new NotFoundException("User not found"));

        if (!user.isActivated()) {
            throw new UnathorizedException("Email not verified");
        }

        if (!user.isEnabled()) {
            throw new ForbiddenException("Account suspended. Please contact support");
        }

        if (profileType == ProfileType.PRINTER && user.getPrinterProfile() == null) {
            throw new ForbiddenException("You do not have a printer profile attached to this email");
        }

        if (profileType == ProfileType.DESIGNER && user.getDesignerProfile() == null) {
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

    @Override
    public Address getAddress() {
        User user = getCurrentUser();
        return addressRepository.findByUser(user).orElse(createAddress(user));
    }

    @Override
    public MailSetting getMailSetting() {
        User user = getCurrentUser();
        return mailSettingRepository.findByUser(user).orElse(createMailSetting(user));
    }

    @Override
    public PaymentDetail getPaymentDetails() {
        return paymentDetailRepository.findByUser(getCurrentUser()).orElse(createPaymentDetails(getCurrentUser()));
    }

    private void validateDesignerRegistration(RegistrationRequest dto) {
        if (dto.getBusinessName() == null) {
            throw new BadRequestException("Shop name is required");
        }

        if (dto.getBusinessName().trim().length() < 3) {
            throw new BadRequestException("Shop name too short, minimum character is 3");
        }

        if (profileRepository.existsByProfileTypeAndNameAllIgnoreCase(ProfileType.DESIGNER, dto.getBusinessName())) {
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

        if (profileRepository.existsByProfileTypeAndNameAllIgnoreCase(ProfileType.PRINTER, dto.getBusinessName())) {
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

        if (profileRepository.existsByProfileTypeAndNameAllIgnoreCase(ProfileType.CUSTOMER, dto.getUsername())) {
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

    private User createUser(RegistrationRequest dto, ProfileType profileType) {

        String userName = createUserName(dto);
        User user = new User();
        user.setEmail(dto.getEmail().trim());
        user.setName(dto.getName().trim());
        user.setEnabled(false);
        user.setRoles(getRoles(profileType));
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setActivated(false);

        user =  userRepository.save(user);
        createCustomerProfile(userName, user);
        createMailSetting(user, dto);
        createAddress(user);
        createPaymentDetails(user);
        createProfile(dto.getBusinessName(), profileType, user);
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
        while (profileRepository.existsByProfileTypeAndNameAllIgnoreCase(ProfileType.CUSTOMER, name)) {
            name = name + RandomStringUtils.randomAlphanumeric(3, 6);
        }
        return name;
    }

    private void createProfile(String name, ProfileType profileType, User user) {
        if (profileType == ProfileType.CUSTOMER) {
            return;
        }

        Profile profile = new Profile();
        profile.setName(name);
        profile.setUser(user);
        profile.setProfileType(profileType);
        profile = profileRepository.save(profile);
        createRating(profile);

        if (profileType == ProfileType.DESIGNER) {
            user.setDesignerProfile(profile);
            Role designerAuthority = authorityRepository.findByName(RoleName.ROLE_DESIGNER).orElseThrow(() ->new BadRequestException("Unable to complete Registration at the moment"));
            user.getRoles().add(designerAuthority);
            return;
        }
        Role printerAuthority = authorityRepository.findByName(RoleName.ROLE_PRINTER).orElseThrow(() ->new BadRequestException("Unable to complete Registration at the moment"));
        user.setPrinterProfile(profile);
        user.getRoles().add(printerAuthority);
    }

    private void createCustomerProfile(String name,  User user) {
        Profile profile = new Profile();
        profile.setName(name);
        profile.setUser(user);
        profile.setProfileType(ProfileType.CUSTOMER);
        profile = profileRepository.save(profile);
        user.setCustomerProfile(profile);
    }

    private void createMailSetting(User user, RegistrationRequest dto) {
        MailSetting mailSetting = new MailSetting();
        mailSetting.setPromotionEmail(dto.isSendPromotionEmail());
        mailSetting.setSupportEmail(dto.isSendPromotionEmail());
        mailSetting.setOrderEmail(true);
        mailSetting.setOtherEmail(true);
        mailSetting.setNewsEmail(true);
        mailSetting.setUser(user);
        mailSettingRepository.save(mailSetting);
    }

    private MailSetting createMailSetting(User user) {
        MailSetting mailSetting = new MailSetting();
        mailSetting.setOrderEmail(true);
        mailSetting.setOtherEmail(true);
        mailSetting.setNewsEmail(true);
        mailSetting.setUser(user);
        mailSettingRepository.save(mailSetting);
        return mailSetting;
    }

    private void updateAddress(UpdateUserRequest request, User user, String country) {

        Address address = addressRepository.findByUser(user).orElse(new Address());
        address.setAddress(request.getAddress());
        address.setCity(request.getCity());
        address.setCountry(country);
        address.setPostalCode(request.getPostalCode());
        address.setState(request.getState());
        address.setUser(user);
        addressRepository.save(address);
    }

    private Address createAddress(User user) {
        Address address = addressRepository.findByUser(user).orElse(new Address());
        address.setUser(user);
        addressRepository.save(address);
        return address;
    }

    private PaymentDetail createPaymentDetails(User user) {
        PaymentDetail paymentDetail = paymentDetailRepository.findByUser(user).orElse(new PaymentDetail());
        paymentDetail.setUser(user);
        paymentDetail = paymentDetailRepository.save(paymentDetail);
        return paymentDetail;
    }

    private void createRating(Profile profile) {
        Rating rating = ratingRepository.findByProfile(profile).orElse(new Rating());
        rating.setProfile(profile);
        ratingRepository.save(rating);
    }

    private User getCurrentUser() {
        return userRepository.findFirstByEmail(tokenProvider.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
