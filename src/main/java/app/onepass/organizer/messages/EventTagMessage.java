package app.onepass.organizer.messages;

import app.onepass.apis.EventTag;
import app.onepass.organizer.entities.EventTagEntity;
import lombok.Getter;

public class EventTagMessage implements BaseMessage<EventTagMessage, EventTagEntity> {

	@Getter
	EventTag eventTag;

	public EventTagMessage(EventTag eventTag) {
		this.eventTag = eventTag;
	}

	@Override
	public EventTagEntity parseMessage() {
		return EventTagEntity.builder().id(eventTag.getId()).eventId(eventTag.getEventId()).tagId(eventTag.getTagId()).build();
	}
}