package app.onepass.organizer.utilities;

import app.onepass.apis.Organization;
import app.onepass.organizer.entities.OrganizationEntity;

public class EntityParser {

	public static Organization parseOrganizationEntity(OrganizationEntity organizationEntity) {
		return Organization.newBuilder()
				.setId(organizationEntity.getId())
				.setName(organizationEntity.getName())
				.setIsVerified(organizationEntity.is_verified()).build();
	}

	public static OrganizationEntity parseOrganization(Organization organization) {
		return OrganizationEntity.builder()
				.id(organization.getId())
				.name(organization.getName())
				.is_verified(organization.getIsVerified()).build();
	}

}
