package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WithoutSpacesValidator implements ConstraintValidator<WithoutSpaces, String>  {
    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        return login != null && !login.contains(" ");
    }
}
