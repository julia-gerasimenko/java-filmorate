package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FilmReleaseDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD}) //показываем, что мы можем пометить этой аннотацией.
@Retention(RetentionPolicy.RUNTIME)
public @interface FilmReleaseDate {
    String message() default "{value.tooOld}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
