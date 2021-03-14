package app.onepass.organizer.messages;

import app.onepass.apis.Event;
import app.onepass.organizer.entities.EventEntity;
import lombok.Getter;

public class EventMessage implements BaseMessage<EventMessage, EventEntity> {

	public EventMessage(Event event) {
		this.event = event;
	}

	@Getter
	Event event;

	@Override
	public EventEntity parseMessage() {

		return EventEntity.builder()
				.id(event.getId())
				.organizationId(event.getOrganizationId())
				.eventLocationId(event.getEventLocationId())
				.description(event.getDescription())
				.name(event.getName())
				.coverImage(String.valueOf(event.getCoverImage()))
				.coverImageHash(String.valueOf(event.getCoverImageHash()))
				.posterImage(String.valueOf(event.getPosterImage()))
				.posterImageHash(String.valueOf(event.getPosterImageHash()))
				.build();
	}
}
