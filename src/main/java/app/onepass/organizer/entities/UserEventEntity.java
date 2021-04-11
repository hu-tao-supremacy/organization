package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;

import app.onepass.apis.UserEvent;
import app.onepass.organizer.messages.UserEventMessage;
import app.onepass.organizer.utilities.TypeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_event")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEventEntity implements BaseEntity<UserEventMessage, UserEventEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int userId;
	private int eventId;
	private Integer rating;
	private String ticket;
	@NotNull
	private String status;

	@Override
	public UserEventMessage parseEntity() {

		UserEvent userEvent = UserEvent.newBuilder()
				.setId(id)
				.setUserId(userId)
				.setEventId(eventId)
				.setStatus(TypeUtil.toStatus(status))
				.build();

		if (rating != null) {
			userEvent.toBuilder().setRating(Int32Value.of(rating)).build();
		}

		if (ticket != null) {
			userEvent.toBuilder().setTicket(StringValue.of(ticket)).build();
		}

		return new UserEventMessage(userEvent);
	}
}