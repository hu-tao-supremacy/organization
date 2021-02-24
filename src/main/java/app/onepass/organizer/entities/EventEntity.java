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
@Table(name = "event")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity extends BaseEntity {

	@Id
	@GeneratedValue
	private long id;
	@NotNull
	private long organization_id;
	private long event_location_id;
	@NotNull
	private String description;
	@NotNull
	private String name;
	private String cover_image;
	private String cover_image_hash;
	private String poster_image;
	private String poster_image_hash;
	@NotNull
	private String contact;
}
