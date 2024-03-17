package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Friendship {
	Long userId;
	Long friendId;
	Boolean friendshipStatus;
}
