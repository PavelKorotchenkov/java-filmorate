package ru.yandex.practicum.filmorate.model;

import lombok.*;

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

}
