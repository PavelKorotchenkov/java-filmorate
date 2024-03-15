package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Friendship {
	Long userId;
	Long friendId;
	Boolean friendshipStatus;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Friendship that = (Friendship) o;
		return Objects.equals(userId, that.userId) && Objects.equals(friendId, that.friendId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, friendId);
	}
}
