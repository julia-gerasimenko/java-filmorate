package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @JsonIgnore
    final Set<Long> friends = new HashSet<>();
}

