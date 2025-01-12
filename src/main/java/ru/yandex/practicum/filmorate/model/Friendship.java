package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class Friendship {

    @NotNull
    private Integer userId;

    @NotNull
    private Integer friendId;

    private Boolean approved;

}