package app.onepass.organizer.messages;

import app.onepass.apis.Location;
import app.onepass.organizer.entities.LocationEntity;
import lombok.Getter;

public class LocationMessage implements BaseMessage<LocationMessage, LocationEntity> {

	@Getter
	Location location;

	public LocationMessage(Location location) {
		this.location = location;
	}

	@Override
	public LocationEntity parseMessage() {

		return LocationEntity.builder()
				.id(location.getId())
				.name(location.getName())
				.googleMapUrl(location.getGoogleMapUrl())
				.description(location.hasDescription() ? location.getDescription().getValue() : null)
				.travelInformationImageUrl(location.hasTravelInformationImageUrl() ?
						location.getTravelInformationImageUrl().getValue() : null)
				.travelInformationImageHash(location.hasTravelInformationImageHash() ?
						location.getTravelInformationImageHash().getValue() : null)
				.build();
	}
}
