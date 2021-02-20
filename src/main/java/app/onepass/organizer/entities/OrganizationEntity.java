package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;

@Entity
@Getter
public class OrganizationEntity {

	@Id
	@GeneratedValue
	private long id;
	private String name;
	private boolean is_verified;
}
