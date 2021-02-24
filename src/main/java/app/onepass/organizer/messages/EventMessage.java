package app.onepass.organizer.messages;

import app.onepass.apis.Event;
import app.onepass.organizer.entities.EventEntity;

public class EventMessage implements BaseMessage<Event, EventEntity> {

	@Override
	public EventEntity parseMessage() {
		return null;
	}
}
