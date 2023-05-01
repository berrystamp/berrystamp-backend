package com.berryinkstamp.berrybackendservice.runner;

import com.berryinkstamp.berrybackendservice.enums.RoleName;
import com.berryinkstamp.berrybackendservice.models.Role;
import com.berryinkstamp.berrybackendservice.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStarterRunner implements ApplicationRunner {

    private final RoleRepository authorityRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        for (RoleName authorityName : RoleName.values()) {
            Optional<Role> auth = authorityRepository.findByName(authorityName);
            if (auth.isPresent()) {
                return;
            }
            Role authority = new Role();
            authority.setName(authorityName);
            authorityRepository.save(authority);
        }


    }
}
