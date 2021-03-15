package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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

	@Override
	public OrganizationMessage parseEntity() {

		Organization organization = Organization.newBuilder()
				.setId(id)
				.setName(name)
				.setIsVerified(isVerified)
				.build();

		return new OrganizationMessage(organization);
	}
}
