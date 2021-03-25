package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.protobuf.StringValue;

import app.onepass.apis.Organization;
import app.onepass.organizer.messages.OrganizationMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organization")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationEntity implements BaseEntity<OrganizationMessage, OrganizationEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotNull
	private String name;
	private boolean isVerified;
	private String abbreviation;
	private String advisor;
	private String associatedFaculty;
	private String description;
	private String facebookPage;
	private String instagram;
	private String lineOfficialAccount;
	private String email;
	private String contactFullName;
	private String contactEmail;
	private String contactPhoneNumber;
	private String contactLineId;
	private String profilePictureUrl;
	private String profilePictureHash;

	@Override
	public OrganizationMessage parseEntity() {

		Organization organization = Organization.newBuilder()
				.setId(id)
				.setName(name)
				.setIsVerified(isVerified)
				.build();

		if (abbreviation != null) {
			organization = organization.toBuilder().setAbbreviation(StringValue.of(abbreviation)).build();
		}

		if (advisor != null) {
			organization = organization.toBuilder().setAdvisor(StringValue.of(advisor)).build();
		}

		if (associatedFaculty != null) {
			organization = organization.toBuilder().setAssociatedFaculty(StringValue.of(associatedFaculty)).build();
		}

		if (description != null) {
			organization = organization.toBuilder().setDescription(StringValue.of(description)).build();
		}

		if (facebookPage != null) {
			organization = organization.toBuilder().setFacebookPage(StringValue.of(facebookPage)).build();
		}

		if (instagram != null) {
			organization = organization.toBuilder().setInstagram(StringValue.of(instagram)).build();
		}

		if (lineOfficialAccount != null) {
			organization = organization.toBuilder().setLineOfficialAccount(StringValue.of(lineOfficialAccount)).build();
		}

		if (email != null) {
			organization = organization.toBuilder().setEmail(StringValue.of(email)).build();
		}

		if (contactFullName != null) {
			organization = organization.toBuilder().setContactFullName(StringValue.of(contactFullName)).build();
		}

		if (contactEmail != null) {
			organization = organization.toBuilder().setContactEmail(StringValue.of(contactEmail)).build();
		}

		if (contactPhoneNumber != null) {
			organization = organization.toBuilder().setContactPhoneNumber(StringValue.of(contactPhoneNumber)).build();
		}

		if (contactLineId != null) {
			organization = organization.toBuilder().setContactLineId(StringValue.of(contactLineId)).build();
		}

		if (profilePictureUrl != null) {
			organization = organization.toBuilder().setProfilePictureUrl(StringValue.of(profilePictureUrl)).build();
		}

		if (profilePictureHash != null) {
			organization = organization.toBuilder().setProfilePictureHash(StringValue.of(profilePictureHash)).build();
		}

		return new OrganizationMessage(organization);
	}
}
