package com.berryinkstamp.berrybackendservice.validators;

import com.berryinkstamp.berrybackendservice.annotations.ValidPrinter;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.repositories.ProfileRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

@Component
public class PrinterValidator implements ConstraintValidator<ValidPrinter, Long> {

    private final ProfileRepository profileRepository;

    public PrinterValidator(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public boolean isValid(final Long value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }


        Optional<Profile> profile = profileRepository.findById(value);
        if (profile.isEmpty()) {
            return false;
        }

        return profile.get().getProfileType() == ProfileType.PRINTER;
    }


}