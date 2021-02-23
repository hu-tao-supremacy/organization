package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "facility")
@Getter
@Builder
public class FacilityEntity {

	public FacilityEntity() {}

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
