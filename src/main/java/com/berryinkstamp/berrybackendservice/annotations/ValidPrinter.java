package com.berryinkstamp.berrybackendservice.annotations;

import com.berryinkstamp.berrybackendservice.validators.PrinterValidator;
import com.berryinkstamp.berrybackendservice.validators.UrlValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {PrinterValidator.class})
@Retention(RUNTIME)
@Target({ElementType.FIELD})
public @interface ValidPrinter {
    String message() default "Invalid printer Id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
