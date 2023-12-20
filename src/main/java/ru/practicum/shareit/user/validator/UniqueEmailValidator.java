package ru.practicum.shareit.user.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.annotation.UniqueEmail;
import ru.practicum.shareit.user.repository.impl.UserRepositoryImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserRepositoryImpl userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (userRepository.findByEmail(email) != null) {
            throw new ConflictException("Данный email уже существует");
        }
        return true;
    }
}
