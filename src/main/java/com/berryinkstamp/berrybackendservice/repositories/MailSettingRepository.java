package com.berryinkstamp.berrybackendservice.repositories;

import com.berryinkstamp.berrybackendservice.models.MailSetting;
import com.berryinkstamp.berrybackendservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailSettingRepository extends JpaRepository<MailSetting, Long> {
    Optional<MailSetting> findByUser(User user);

}