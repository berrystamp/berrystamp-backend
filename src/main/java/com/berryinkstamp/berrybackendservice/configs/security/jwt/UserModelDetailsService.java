package com.berryinkstamp.berrybackendservice.configs.security.jwt;

import com.berryinkstamp.berrybackendservice.exceptions.UserNotActivatedException;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.repositories.UserRepository;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class UserModelDetailsService implements UserDetailsService {

   private final Logger log = LoggerFactory.getLogger(UserModelDetailsService.class);

   private final UserRepository userRepository;

   public UserModelDetailsService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String login) {
      log.debug("Authenticating user '{}'", login);

      if (new EmailValidator().isValid(login, null)) {
         return userRepository.findFirstByEmail(login)
            .map(user -> createSpringSecurityUser(login, user))
            .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
      }

      String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
      return userRepository.findFirstByEmail(lowercaseLogin)
         .map(user -> createSpringSecurityUser(lowercaseLogin, user))
         .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));

   }

   private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
      if (!user.isActivated()) {
         throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
      }
      List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
         .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
         .collect(Collectors.toList());
      return new org.springframework.security.core.userdetails.User(user.getEmail(),
         user.getPassword(),
         grantedAuthorities);
   }
}
