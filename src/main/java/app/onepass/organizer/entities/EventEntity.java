package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;

import app.onepass.apis.Event;
import app.onepass.organizer.messages.EventMessage;
import app.onepass.organizer.utilities.TypeUtil;
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
	private int id;
	private int organizationId;
	private Integer locationId;
	@NotNull
	private String description;
	@NotNull
	private String name;
	private String coverImageUrl;
	private String coverImageHash;
	private String posterImageUrl;
	private String posterImageHash;
	private String contact;
	private String profileImageUrl;
	private String profileImageHash;
	private int attendeeLimit;
	private java.sql.Timestamp registrationDueDate;

	@Override
	public EventMessage parseEntity() {

		Event event = Event.newBuilder()
				.setId(id)
				.setOrganizationId(organizationId)
				.setDescription(description)
				.setName(name)
				.setAttendeeLimit(attendeeLimit)
				.build();

		if (locationId != null) {
			event = event.toBuilder().setLocationId(Int32Value.of(locationId)).build();
		}

		if (coverImageUrl != null) {
			event = event.toBuilder().setCoverImageUrl(StringValue.of(coverImageUrl)).build();
		}

		if (coverImageHash != null) {
			event = event.toBuilder().setCoverImageHash(StringValue.of(coverImageHash)).build();
		}

		if (posterImageUrl != null) {
			event = event.toBuilder().setPosterImageUrl(StringValue.of(posterImageUrl)).build();
		}

		if (posterImageHash != null) {
			event = event.toBuilder().setPosterImageHash(StringValue.of(posterImageHash)).build();
		}

		if (profileImageUrl != null) {
			event = event.toBuilder().setProfileImageUrl(StringValue.of(profileImageUrl)).build();
		}

		if (profileImageHash != null) {
			event = event.toBuilder().setProfileImageHash(StringValue.of(profileImageHash)).build();
		}

		if (contact != null) {
			event = event.toBuilder().setContact(StringValue.of(contact)).build();
		}

		if (registrationDueDate != null) {
			com.google.protobuf.Timestamp dueDate = TypeUtil.toProtobufTimestamp(registrationDueDate);
			event = event.toBuilder().setRegistrationDueDate(dueDate).build();
		}

		return new EventMessage(event);
	}
}
