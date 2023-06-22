package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Data
public class User {
    private long id;

    @Email(message = "Email введен неверно")
    @NotEmpty(message = "Email не может быть пустым")
    private String email;

    @Pattern(regexp = "^\\S*", message = "логин не может содержать пробелы")
    @NotBlank(message = "логин не может быть пустым")
    private String login;

    private String name;

    @NotNull(message = "дата рождения не может быть пустой")
    @PastOrPresent(message = "дата рождения не может быть в будущем")
    private LocalDate birthday;
}

