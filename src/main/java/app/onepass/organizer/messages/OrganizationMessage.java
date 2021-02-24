package app.onepass.organizer.messages;

import app.onepass.apis.Organization;
import app.onepass.organizer.entities.OrganizationEntity;

public class OrganizationMessage {

	private Organization organization;

	public OrganizationEntity parseIntoEntity() {
		return OrganizationEntity.builder()
				.id(organization.getId())
				.name(organization.getName())
				.is_verified(organization.getIsVerified()).build();
	}
}
