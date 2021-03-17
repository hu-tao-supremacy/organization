package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import app.onepass.apis.EventRegistration;
import app.onepass.organizer.messages.EventRegistrationMessage;
import app.onepass.organizer.utilities.StatusUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event_registration")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistrationEntity implements BaseEntity<EventRegistrationMessage, EventRegistrationEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long eventId;
	private long userId;
	@NotNull
	private String status;

	@Override
	public EventRegistrationMessage parseEntity() {

		EventRegistration eventRegistration = EventRegistration.newBuilder()
				.setId(id)
				.setEventId(eventId)
				.setUserId(userId)
				.setStatus(StatusUtil.toStatus(status))
				.build();

		return new EventRegistrationMessage(eventRegistration);
	}
}

