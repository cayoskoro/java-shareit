package ru.practicum.shareit.user.annotation;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.validator.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
@Documented
public @interface UniqueEmail {
    String message() default "Данный email уже существует";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}