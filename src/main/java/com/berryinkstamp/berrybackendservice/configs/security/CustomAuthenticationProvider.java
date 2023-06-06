package com.berryinkstamp.berrybackendservice.configs.security;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.AdminModelDetailsService;
import com.berryinkstamp.berrybackendservice.configs.security.jwt.UserModelDetailsService;
import com.berryinkstamp.berrybackendservice.enums.AuthProvider;
import com.berryinkstamp.berrybackendservice.exceptions.BadRequestException;
import com.berryinkstamp.berrybackendservice.models.Admin;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.repositories.AdminRepository;
import com.berryinkstamp.berrybackendservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserModelDetailsService userModelDetailsService;

    private final AdminModelDetailsService adminModelDetailsService;

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthenticationToken authenticationToken = (CustomAuthenticationToken) authentication;
        String principal = authenticationToken.getCredentials().toString();
        String password = authenticationToken.getPassword() == null ? "" : authenticationToken.getPassword();
        if (authenticationToken.getAuthProvider() == AuthProvider.activation) {
            return userEmailAuthentication(principal);
        }

        if (authenticationToken.getAuthProvider() == AuthProvider.admin) {
            return adminEmailAndPasswordAuthentication(principal, password);
        }

        if (authenticationToken.getAuthProvider() == AuthProvider.local) {
            return userEmailAndPasswordAuthentication(principal, password);
        }

        throw new BadCredentialsException("Authentocation failed");

    }

    private Authentication userEmailAuthentication(String principal) {
        User user = userRepository.findFirstByEmail(principal).orElseThrow(() -> new BadRequestException("Authentication failed"));
        List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(principal, "password", grantedAuthorities);
    }

    private Authentication userEmailAndPasswordAuthentication(String principal, String password) {
        User user = userRepository.findFirstByEmail(principal).orElseThrow(() -> new BadRequestException("Authentication failed"));
        List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
        DBAuthenticationProvider authenticationProvider = new DBAuthenticationProvider(userModelDetailsService, adminModelDetailsService);
        return authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(principal, password, grantedAuthorities));
    }

    private Authentication adminEmailAndPasswordAuthentication(String principal, String password) {
        Admin user = adminRepository.findFirstByEmail(principal).orElseThrow(() -> new BadRequestException("Authentication failed"));
        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
        DBAuthenticationProvider authenticationProvider = new DBAuthenticationProvider(userModelDetailsService, adminModelDetailsService);
        return authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(principal, password, grantedAuthorities));
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(CustomAuthenticationToken.class);
    }
}

