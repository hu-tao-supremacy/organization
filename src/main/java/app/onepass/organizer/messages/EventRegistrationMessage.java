package app.onepass.organizer.messages;

import app.onepass.apis.EventRegistration;
import app.onepass.organizer.entities.EventRegistrationEntity;
import lombok.Getter;

public class EventRegistrationMessage implements BaseMessage<EventRegistrationMessage, EventRegistrationEntity> {

	public EventRegistrationMessage(EventRegistration eventRegistration) { this.eventRegistration = eventRegistration; }

	@Getter
	EventRegistration eventRegistration;

	@Override
	public EventRegistrationEntity parseMessage() {

		return EventRegistrationEntity.builder()
				.id(eventRegistration.getId())
				.eventId(eventRegistration.getEventId())
				.userId(eventRegistration.getUserId())
				.status(eventRegistration.getStatus())
				.build();
	}
}
