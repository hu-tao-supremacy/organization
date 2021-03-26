package app.onepass.organizer.messages;

import app.onepass.apis.Organization;
import app.onepass.organizer.entities.OrganizationEntity;
import lombok.Getter;

public class OrganizationMessage implements BaseMessage<OrganizationMessage, OrganizationEntity> {

	public OrganizationMessage(Organization organization) {
		this.organization = organization;
	}

	@Getter
	Organization organization;

	@Override
	public OrganizationEntity parseMessage() {
		return OrganizationEntity.builder()
				.id(organization.getId())
				.name(organization.getName())
				.isVerified(organization.getIsVerified())
				.abbreviation(organization.getAbbreviation().getValue())
				.advisor(organization.getAdvisor().getValue())
				.associatedFaculty(organization.getAssociatedFaculty().getValue())
				.description(organization.getDescription().getValue())
				.facebookPage(organization.getFacebookPage().getValue())
				.instagram(organization.getInstagram().getValue())
				.lineOfficialAccount(organization.getLineOfficialAccount().getValue())
				.email(organization.getEmail().getValue())
				.contactFullName(organization.getContactFullName().getValue())
				.contactEmail(organization.getContactEmail().getValue())
				.contactPhoneNumber(organization.getContactPhoneNumber().getValue())
				.contactLineId(organization.getContactLineId().getValue())
				.profilePictureUrl(organization.getProfilePictureUrl().getValue())
				.profilePictureHash(organization.getProfilePictureHash().getValue())
				.build();
	}
}
