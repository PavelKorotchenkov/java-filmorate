package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateNotBeforeValidator.class)
public @interface LocalDateNotBefore {
	String message() default "Date cannot be before December 12, 1895";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}