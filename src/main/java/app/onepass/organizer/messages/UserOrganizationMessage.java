package app.onepass.organizer.messages;

import app.onepass.apis.UserOrganization;
import app.onepass.organizer.entities.UserOrganizationEntity;
import lombok.Getter;

public class UserOrganizationMessage implements BaseMessage<UserOrganizationMessage, UserOrganizationEntity> {

	@Getter
	UserOrganization userOrganization;

	public UserOrganizationMessage(UserOrganization userOrganization) {
		this.userOrganization = userOrganization;
	}

	@Override
	public UserOrganizationEntity parseMessage() {
		return UserOrganizationEntity.builder()
				.id(userOrganization.getId())
				.userId(userOrganization.getUserId())
				.organizationId(userOrganization.getOrganizationId())
				.build();
	}
}