package app.onepass.organizer.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import app.onepass.apis.Facility;
import app.onepass.apis.OperatingHour;
import app.onepass.organizer.messages.FacilityMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "facility")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityEntity implements BaseEntity<FacilityMessage, FacilityEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long organizationId;
	@NotNull
	private String name;
	private double latitude;
	private double longitude;
	@NotNull
	private transient List<OperatingHour> operatingHours;
	@NotNull
	private String description;

	@Override
	public FacilityMessage parseEntity() {

		Facility facility = Facility.newBuilder()
				.setId(id)
				.setOrganizationId(organizationId)
				.setName(name)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.addAllOperatingHours(operatingHours)
				.setDescription(description)
				.build();

		return new FacilityMessage(facility);
	}

}
