package app.onepass.organizer.messages;

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
		return EventDurationEntity.builder()
				.id(eventDuration.getId())
				.eventId(eventDuration.getEventId())
				.start(eventDuration.getStart())
				.finish(eventDuration.getFinish())
				.build();
	}
}