package app.onepass.organizer.utilities;

import app.onepass.apis.Organization;
import app.onepass.organizer.entities.OrganizationEntity;

public class EntityParser {

	public static Organization parseOrganization(OrganizationEntity organizationEntity) {
		return Organization.newBuilder().setId(organizationEntity.getId())
				.setName(organizationEntity.getName())
				.setIsVerified(organizationEntity.is_verified()).build();
	}

}
