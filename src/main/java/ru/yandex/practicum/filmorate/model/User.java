package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.CorrectBirthday;
import ru.yandex.practicum.filmorate.validators.WithoutSpaces;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {
    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    private int id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @WithoutSpaces
    private String login;

    private String name;

    @CorrectBirthday
    private LocalDate birthday;

    public Set<Integer> friends = new HashSet<>();
}