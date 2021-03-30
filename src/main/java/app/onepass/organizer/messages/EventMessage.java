package app.onepass.organizer.messages;

import app.onepass.apis.Event;
import app.onepass.organizer.entities.EventEntity;
import lombok.Getter;

public class EventMessage implements BaseMessage<EventMessage, EventEntity> {

	@Getter
	Event event;

	public EventMessage(Event event) {
		this.event = event;
	}

	@Override
	public EventEntity parseMessage() {

		return EventEntity.builder()
				.id(event.getId())
				.organizationId(event.getOrganizationId())
				.locationId(event.getLocationId().getValue())
				.description(event.getDescription())
				.name(event.getName())
				.coverImageUrl(event.getCoverImageUrl().getValue())
				.coverImageHash(event.getCoverImageHash().getValue())
				.posterImageUrl(event.getPosterImageUrl().getValue())
				.posterImageHash(event.getPosterImageHash().getValue())
				.contact(event.getContact())
				.profileImageUrl(event.getProfileImageUrl().getValue())
				.profileImageHash(event.getProfileImageHash().getValue())
				.build();
	}
}
