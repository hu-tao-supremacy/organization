package app.onepass.organizer.messages;

import app.onepass.apis.Organization;
import app.onepass.organizer.entities.OrganizationEntity;
import lombok.Getter;

public class OrganizationMessage implements BaseMessage<OrganizationMessage, OrganizationEntity> {

	@Getter
	Organization organization;

	public OrganizationMessage(Organization organization) {
		this.organization = organization;
	}

	@Override
	public OrganizationEntity parseMessage() {
		return OrganizationEntity.builder()
				.id(organization.getId())
				.name(organization.getName())
				.isVerified(organization.getIsVerified())
				.abbreviation(organization.hasAbbreviation() ? organization.getAbbreviation().getValue() : null)
				.advisor(organization.hasAdvisor() ? organization.getAdvisor().getValue() : null)
				.associatedFaculty(organization.hasAssociatedFaculty() ? organization.getAssociatedFaculty().getValue() : null)
				.description(organization.hasDescription() ? organization.getDescription().getValue() : null)
				.facebookPage(organization.hasFacebookPage() ? organization.getFacebookPage().getValue() : null)
				.instagram(organization.hasInstagram() ? organization.getInstagram().getValue() : null)
				.lineOfficialAccount(organization.hasLineOfficialAccount() ? organization.getLineOfficialAccount().getValue() : null)
				.email(organization.hasEmail() ? organization.getEmail().getValue() : null)
				.contactFullName(organization.hasContactFullName() ? organization.getContactFullName().getValue() : null)
				.contactEmail(organization.hasContactEmail() ? organization.getContactEmail().getValue() : null)
				.contactPhoneNumber(organization.hasContactPhoneNumber() ? organization.getContactPhoneNumber().getValue() : null)
				.contactLineId(organization.hasContactLineId() ? organization.getContactLineId().getValue() : null)
				.profilePictureUrl(organization.hasProfilePictureUrl() ? organization.getProfilePictureUrl().getValue() : null)
				.profilePictureHash(organization.hasProfilePictureHash() ? organization.getProfilePictureHash().getValue() : null)
				.build();
	}
}
