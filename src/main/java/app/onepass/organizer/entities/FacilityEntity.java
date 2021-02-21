package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "facility")
@Getter
public class FacilityEntity {

	@Id
	@GeneratedValue
	private long id;
	private long organization_id;
	private String name;
	private double latitude;
	private double longitude;
	private String operating_hours;
	private String description;
}
