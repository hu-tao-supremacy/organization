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
				.locationId(event.hasLocationId() ? event.getLocationId().getValue() : null)
				.description(event.getDescription())
				.name(event.getName())
				.attendeeLimit(event.getAttendeeLimit())
				.coverImageUrl(event.hasCoverImageUrl() ? event.getCoverImageUrl().getValue() : null)
				.coverImageHash(event.hasCoverImageHash() ? event.getCoverImageHash().getValue() : null)
				.posterImageUrl(event.hasPosterImageUrl() ? event.getPosterImageUrl().getValue() : null)
				.posterImageHash(event.hasPosterImageHash() ? event.getPosterImageHash().getValue() : null)
				.contact(event.hasContact() ? event.getContact().getValue() : null)
				.profileImageUrl(event.hasProfileImageUrl() ? event.getProfileImageUrl().getValue() : null)
				.profileImageHash(event.hasProfileImageHash() ? event.getProfileImageHash().getValue() : null)
				.build();
	}
}
