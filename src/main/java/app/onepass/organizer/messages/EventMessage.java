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
				.eventLocationId(event.getEventLocationId().getValue())
				.description(event.getDescription())
				.name(event.getName())
				.coverImage(event.getCoverImage().getValue())
				.coverImageHash(event.getCoverImageHash().getValue())
				.posterImage(event.getPosterImage().getValue())
				.posterImageHash(event.getPosterImageHash().getValue())
				.build();
	}
}
