package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class LocalDateNotBeforeValidator implements ConstraintValidator<LocalDateNotBefore, LocalDate> {

	@Override
	public void initialize(LocalDateNotBefore constraintAnnotation) {
	}

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		LocalDate minDate = LocalDate.of(1895, 12, 28);
		return !value.isBefore(minDate);
	}
}