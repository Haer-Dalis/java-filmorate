package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class User {
    public User(@NonNull String email, @NonNull String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
    
    public User() {
    }

    private int id;
    @NotNull
    @NotBlank
    @NotNull private String email;
    @NotNull
    @NotBlank
    @NotNull private String login;
    private String name;
    private LocalDate birthday;
}
