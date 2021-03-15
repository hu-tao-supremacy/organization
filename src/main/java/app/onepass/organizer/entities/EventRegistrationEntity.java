package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import app.onepass.apis.EventRegistration;
import app.onepass.apis.Status;
import app.onepass.organizer.messages.EventRegistrationMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event_duration")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistrationEntity implements BaseEntity<EventRegistrationMessage, EventRegistrationEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotNull
	private long eventId;
	@NotNull
	private long userId;
	@NotNull
	private Status status;

	@Override
	public EventRegistrationMessage parseEntity() {

		EventRegistration eventRegistration = EventRegistration.newBuilder()
				.setId(id)
				.setEventId(eventId)
				.setUserId(userId)
				.setStatus(status)
				.build();

		return new EventRegistrationMessage(eventRegistration);
	}
}

