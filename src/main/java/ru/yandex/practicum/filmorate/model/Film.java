package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.ReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@AllArgsConstructor
public class Film {

    private int id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @ReleaseDate
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private Set<Integer> likes;

    @JsonProperty("mpa")
    private MpaRating mpaRating;

    private Set<Genre> genres;

    public int getLikesCount() {
        if (likes == null) {
            return 0;
        } else {
            return likes.size();
        }
    }


}