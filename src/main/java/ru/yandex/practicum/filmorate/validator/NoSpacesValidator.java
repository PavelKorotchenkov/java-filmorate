package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoSpacesValidator implements ConstraintValidator<NoSpaces, String> {

	@Override
	public void initialize(NoSpaces constraintAnnotation) {
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		return s == null || !s.contains(" ");
	}
}
