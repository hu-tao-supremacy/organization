package app.onepass.organizer.messages;

import app.onepass.apis.Organization;
import app.onepass.organizer.entities.OrganizationEntity;

public class OrganizationMessage implements BaseMessage<Organization, OrganizationEntity>{

	Organization organization;

	@Override
	public OrganizationEntity parseMessage() {
		return OrganizationEntity.builder()
				.id(organization.getId())
				.name(organization.getName())
				.is_verified(organization.getIsVerified())
				.build();
	}
}
