package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class BirthdayValidator implements ConstraintValidator<CorrectBirthday, LocalDate> {
    @Override
    public boolean isValid(LocalDate birthday, ConstraintValidatorContext context) {
        return birthday != null && !birthday.isAfter(LocalDate.now());
    }
}