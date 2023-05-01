package com.berryinkstamp.berrybackendservice.annotations;

import com.berryinkstamp.berrybackendservice.validators.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {PasswordValidator.class})
@Retention(RUNTIME)
@Target({ElementType.FIELD})
public @interface ValidPassword {
    String message() default "Password must contain at least one digit, one lowercase character, one upper character, one special character and must be of length 8 - 20.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
