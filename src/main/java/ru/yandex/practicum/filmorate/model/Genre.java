package ru.yandex.practicum.filmorate.model;

import lombok.*;

import jakarta.validation.constraints.*;

@Data
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@Builder
public class Genre {

    @Min(1)
    private Integer id;

    @NotBlank
    private String name;

}