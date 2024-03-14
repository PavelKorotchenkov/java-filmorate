package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Director {
    private Long id;
    @NotBlank
    @EqualsAndHashCode.Exclude
    private String name;
}
