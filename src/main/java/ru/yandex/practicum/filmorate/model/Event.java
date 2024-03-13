package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Event {
	private Long eventId;
	private Long userId;
	private Long entityId;
	private EventOperation operation;
	private EventType eventType;
	private Long timestamp;

	public Event(Long userId, Long entityId, EventOperation operation, EventType eventType, Long timestamp) {
		this.userId = userId;
		this.entityId = entityId;
		this.operation = operation;
		this.eventType = eventType;
		this.timestamp = timestamp;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> values = new HashMap<>();
		values.put("event_id", eventId);
		values.put("user_id", userId);
		values.put("entity_id", entityId);
		values.put("operation", operation);
		values.put("event_type", eventType);
		values.put("event_timestamp", timestamp);
		return values;
	}
}
