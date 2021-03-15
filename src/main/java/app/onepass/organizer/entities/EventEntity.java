package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;

import app.onepass.apis.Event;
import app.onepass.organizer.messages.EventMessage;
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
public class EventEntity implements BaseEntity<EventMessage, EventEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long organizationId;
	private Long eventLocationId;
	@NotNull
	private String description;
	@NotNull
	private String name;
	private String coverImage;
	private String coverImageHash;
	private String posterImage;
	private String posterImageHash;
	@NotNull
	private String contact;

	@Override
	public EventMessage parseEntity() {

		StringValue cover = coverImage == null ? null : StringValue.of(coverImage);
		StringValue coverHash = coverImageHash == null ? null : StringValue.of(coverImageHash);
		StringValue poster = posterImage == null ? null : StringValue.of(posterImage);
		StringValue posterHash = posterImageHash == null ? null : StringValue.of(posterImageHash);

		Event event = Event.newBuilder()
				.setId(id)
				.setOrganizationId(organizationId)
				.setEventLocationId(Int64Value.of(eventLocationId))
				.setDescription(description)
				.setName(name)
				.setCoverImage(cover)
				.setCoverImageHash(coverHash)
				.setPosterImage(poster)
				.setPosterImageHash(posterHash)
				.setContact(contact)
				.build();

		return new EventMessage(event);
	}
}
