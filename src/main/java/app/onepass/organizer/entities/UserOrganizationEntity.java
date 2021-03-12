package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import app.onepass.apis.UserOrganization;
import app.onepass.organizer.messages.UserOrganizationMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_organization")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrganizationEntity implements BaseEntity<UserOrganizationMessage, UserOrganizationEntity> {

	@Id
	@GeneratedValue
	private long id;
	@NotNull
	private long userId;
	@NotNull
	private long organizationId;

	@Override
	public UserOrganizationMessage parseEntity() {

		UserOrganization userOrganization = UserOrganization.newBuilder()
				.setId(this.getId())
				.setUserId(this.getUserId())
				.setOrganizationId(this.getOrganizationId())
				.build();

		return new UserOrganizationMessage(userOrganization);
	}
}
