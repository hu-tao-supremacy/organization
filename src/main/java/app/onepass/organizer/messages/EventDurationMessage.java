package app.onepass.organizer.messages;

import java.time.Instant;

import app.onepass.apis.EventDuration;
import app.onepass.organizer.entities.EventDurationEntity;
import lombok.Getter;

public class EventDurationMessage implements BaseMessage<EventDurationMessage, EventDurationEntity> {

	public EventDurationMessage(EventDuration eventDuration) {
		this.eventDuration = eventDuration;
	}

	@Getter
	EventDuration eventDuration;

	@Override
	public EventDurationEntity parseMessage() {

		java.sql.Timestamp start = java.sql.Timestamp.from(
				Instant.ofEpochSecond(
						eventDuration.getStart().getSeconds(),
						eventDuration.getStart().getNanos()));

		java.sql.Timestamp finish = java.sql.Timestamp.from(
				Instant.ofEpochSecond(
						eventDuration.getFinish().getSeconds(),
						eventDuration.getFinish().getNanos()));

		return EventDurationEntity.builder()
				.id(eventDuration.getId())
				.eventId(eventDuration.getEventId())
				.start(start)
				.finish(finish)
				.build();
	}
}