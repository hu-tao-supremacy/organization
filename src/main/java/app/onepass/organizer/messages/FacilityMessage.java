package app.onepass.organizer.messages;

import app.onepass.apis.Facility;
import app.onepass.organizer.entities.FacilityEntity;
import lombok.Getter;

public class FacilityMessage implements BaseMessage<FacilityMessage, FacilityEntity> {

	public FacilityMessage(Facility facility) { this.facility = facility; }

	@Getter
	Facility facility;

	@Override
	public FacilityEntity parseMessage() {

		return FacilityEntity.builder()
				.id(facility.getId())
				.organizationId(facility.getOrganizationId())
				.name(facility.getName())
				.latitude(facility.getLatitude())
				.longitude(facility.getLongitude())
				.operatingHours(facility.getOperatingHoursList())
				.description(facility.getDescription())
				.build();
	}
}
