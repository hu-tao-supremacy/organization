package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "event")
@Getter
public class EventEntity {

	@Id
	@GeneratedValue
	private long id;
	private long organization_id;
	private long event_location_id;
	private String description;
	private String name;
	private String cover_image;
	private String cover_image_hash;
	private String poster_image;
	private String poster_image_hash;
	private String contact;
}