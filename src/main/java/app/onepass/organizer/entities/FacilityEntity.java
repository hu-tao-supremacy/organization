package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
public class FacilityEntity {

	@Id
	@GeneratedValue
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
}
