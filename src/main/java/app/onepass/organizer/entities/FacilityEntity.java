package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
	@NotNull
	private long organization_id;
	@NotNull
	private String name;
	@NotNull
	private double latitude;
	@NotNull
	private double longitude;
	@NotNull
	private String operating_hours;
	@NotNull
	private String description;

	@Override
	public FacilityMessage parseEntity() {
		return null;
	}
}
