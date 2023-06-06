package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.CustomAuthenticationToken;
import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.AddAdminRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.BaseRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.EditAdminRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.LoginRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.ResetPasswordRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.SendEmailRequest;
import com.berryinkstamp.berrybackendservice.dtos.response.LoginResponse;
import com.berryinkstamp.berrybackendservice.dtos.response.Subscriber;
import com.berryinkstamp.berrybackendservice.enums.AuthProvider;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.enums.RoleName;
import com.berryinkstamp.berrybackendservice.exceptions.BadRequestException;
import com.berryinkstamp.berrybackendservice.exceptions.ForbiddenException;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.exceptions.UnathorizedException;
import com.berryinkstamp.berrybackendservice.models.Admin;
import com.berryinkstamp.berrybackendservice.models.Permission;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.repositories.AdminRepository;
import com.berryinkstamp.berrybackendservice.repositories.PermissionRepository;
import com.berryinkstamp.berrybackendservice.services.AdminService;
import com.berryinkstamp.berrybackendservice.services.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final OTPService otpService;

    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final AdminRepository adminRepository;

    private final PermissionRepository permissionRepository;

    private final AuthenticationManager authenticationManager;



    @Override
    public Admin addAdmin(AddAdminRequest request) {
        Admin loggedInUser = tokenProvider.getCurrentAdmin();
        if (!loggedInUser.isSuperAdmin() && !loggedInUser.isAdmin() && !loggedInUser.getPermission().isCanManagerAdmin()) {
            throw new ForbiddenException("you do not have enough permission to manage this resource");
        }

        Optional<Admin> optionalAdmin = adminRepository.findFirstByEmail(request.getEmail());
        if (optionalAdmin.isPresent()) {
            throw new BadRequestException("user already exists");
        }

        Admin admin = new Admin();
        admin.setEmail(request.getEmail());
        admin.setActivated(false);
        admin.setEnabled(true);
        admin.setName(request.getName());
        admin.setRole(request.getRole());
        admin = adminRepository.save(admin);

        admin = createPermission(admin, request);
        //todo push to audit
        otpService.sendAdminResetOTP(admin);

        return admin;
    }

    @Override
    public Object resendAccountActivationCode(BaseRequest request) {
        Admin loggedInUser = tokenProvider.getCurrentAdmin();
        if (!loggedInUser.isSuperAdmin() && !loggedInUser.isAdmin() && !loggedInUser.getPermission().isCanManagerAdmin()) {
            throw new ForbiddenException("you do not have enough permission to manage this resource");
        }
        Admin user = adminRepository.findFirstByEmail(request.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        otpService.sendAdminResetOTP(user);
        return Map.of();
    }

    @Override
    public Object deleteAdminUser(Long adminId) {
        Admin loggedInUser = tokenProvider.getCurrentAdmin();
        if (!loggedInUser.isSuperAdmin() && !loggedInUser.isAdmin() && !loggedInUser.getPermission().isCanManagerAdmin()) {
            throw new ForbiddenException("you do not have enough permission to manage this resource");
        }

        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (optionalAdmin.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        if (optionalAdmin.get().isSuperAdmin() && !loggedInUser.isSuperAdmin()) {
            throw new ForbiddenException("a super admin can only be deleted by another super admin");
        }

        adminRepository.delete(optionalAdmin.get());
        return Map.of();
    }

    @Override
    public Admin editAdminUser(Long adminId, EditAdminRequest request) {
        Admin loggedInUser = tokenProvider.getCurrentAdmin();
        if (!loggedInUser.isSuperAdmin() && !loggedInUser.isAdmin() && !loggedInUser.getPermission().isCanManagerAdmin()) {
            throw new ForbiddenException("you do not have enough permission to manage this resource");
        }

        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (optionalAdmin.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        if (optionalAdmin.get().isSuperAdmin() && !loggedInUser.isSuperAdmin()) {
            throw new ForbiddenException("a super admin can only be updated by another super admin");
        }

        Admin admin = optionalAdmin.get();
        admin.setName(request.getName());
        admin = adminRepository.save(admin);

        Permission permission = admin.getPermission();

        if (request.getRole() == RoleName.ROLE_ADMIN || request.getRole() == RoleName.ROLE_SUPER_ADMIN) {
            permission.setCanManagerAdmin(true);
            permission.setCanManagerBlogs(true);
            permission.setCanManagerDesigns(true);
            permission.setCanManagerEmails(true);
            permission.setCanManagerOrders(true);
            permission.setCanManagerPayments(true);
            permission.setCanManagerTickets(true);
            permission.setCanManagerTransactions(true);
            permission.setCanManagerUsers(true);
        } else {
            permission.setCanManagerAdmin(request.isCanManagerAdmin());
            permission.setCanManagerBlogs(request.isCanManagerBlogs());
            permission.setCanManagerDesigns(request.isCanManagerDesigns());
            permission.setCanManagerEmails(request.isCanManagerEmails());
            permission.setCanManagerOrders(request.isCanManagerOrders());
            permission.setCanManagerPayments(request.isCanManagerPayments());
            permission.setCanManagerTickets(request.isCanManagerTickets());
            permission.setCanManagerTransactions(request.isCanManagerTransactions());
            permission.setCanManagerUsers(request.isCanManagerUsers());
        }

        permission = permissionRepository.save(permission);
        admin.setPermission(permission);
        return admin;
    }


    @Override
    public Object sendEmail(SendEmailRequest request) {
        //todo send email admin
        return null;
    }

    @Override
    public List<Subscriber> getSubscribers() {
        // todo list subscribers
        return null;
    }

    @Override
    public LoginResponse login(LoginRequest dto) {
        Admin user = adminRepository.findFirstByEmail(dto.getEmail()).orElseThrow(()->new NotFoundException("User not found"));

        if (!user.isActivated()) {
            throw new UnathorizedException("Email not verified");
        }

        if (!user.isEnabled()) {
            throw new ForbiddenException("Account suspended. Please contact support");
        }


        CustomAuthenticationToken authenticationToken = new CustomAuthenticationToken(dto.getEmail(), dto.getPassword(), AuthProvider.admin);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = dto.getRememberMe() != null && dto.getRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);

        user.setLastLoginDate(LocalDateTime.now());
        user = adminRepository.save(user);

        //todo push to audit

        return new LoginResponse(user, jwt);

    }

    @Override
    public Object resetPassword(BaseRequest baseRequest) {
        Admin user = adminRepository.findFirstByEmail(baseRequest.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        otpService.sendForgetPasswordOTP(user.getEmail(), user.getName());
        return Map.of();
    }

    @Override
    public Object completeResetPassword(ResetPasswordRequest request) {
        Admin user = adminRepository.findFirstByEmail(request.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        boolean success = otpService.verifyOTP(user.getEmail(), request.getOtp());
        if (!success) {
            throw new BadRequestException("Invalid otp");
        }
        if (user.getPassword() == null) {
            user.setActivated(true);
        }
        String encodePassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodePassword);

        adminRepository.save(user);

        //todo push to queue
        return Map.of();
    }

    private Admin createPermission(Admin admin, AddAdminRequest request) {
        Permission permission  = new Permission();
        permission.setAdmin(admin);
        if (request.getRole() == RoleName.ROLE_ADMIN || request.getRole() == RoleName.ROLE_SUPER_ADMIN) {
            permission = permissionRepository.save(getSuperPermissions(permission));
        } else{
            permission = permissionRepository.save(getPermissions(request, permission));
        }

        admin.setPermission(permission);
        return admin;
    }

    private Permission getSuperPermissions(Permission permission) {
        permission.setCanManagerAdmin(true);
        permission.setCanManagerBlogs(true);
        permission.setCanManagerDesigns(true);
        permission.setCanManagerEmails(true);
        permission.setCanManagerOrders(true);
        permission.setCanManagerPayments(true);
        permission.setCanManagerTickets(true);
        permission.setCanManagerTransactions(true);
        permission.setCanManagerUsers(true);
        return permission;
    }

    private Permission getPermissions(AddAdminRequest addAdminRequest, Permission permission) {
        permission.setCanManagerAdmin(addAdminRequest.isCanManagerAdmin());
        permission.setCanManagerBlogs(addAdminRequest.isCanManagerBlogs());
        permission.setCanManagerDesigns(addAdminRequest.isCanManagerDesigns());
        permission.setCanManagerEmails(addAdminRequest.isCanManagerEmails());
        permission.setCanManagerOrders(addAdminRequest.isCanManagerOrders());
        permission.setCanManagerPayments(addAdminRequest.isCanManagerPayments());
        permission.setCanManagerTickets(addAdminRequest.isCanManagerTickets());
        permission.setCanManagerTransactions(addAdminRequest.isCanManagerTransactions());
        permission.setCanManagerUsers(addAdminRequest.isCanManagerUsers());
        return permission;
    }

}
