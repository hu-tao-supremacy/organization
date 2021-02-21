package app.onepass.organizer.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "organization")
@Getter
public class OrganizationEntity {

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private boolean is_verified;
}
