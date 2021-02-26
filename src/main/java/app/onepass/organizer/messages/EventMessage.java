package app.onepass.organizer.messages;

import app.onepass.organizer.entities.EventEntity;

public class EventMessage implements BaseMessage<EventMessage, EventEntity> {

	@Override
	public EventEntity parseMessage() {
		return null;
	}
}
