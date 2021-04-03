package app.onepass.organizer.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.google.protobuf.Int64Value;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;

import app.onepass.apis.Status;
import app.onepass.apis.UserEvent;
import app.onepass.organizer.messages.UserEventMessage;
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
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class UserEventEntity implements BaseEntity<UserEventMessage, UserEventEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long userId;
	private long eventId;
	private Long rating;
	@NotNull
	private String ticket;
	@NotNull
	@Column(columnDefinition = "user_event_status_enum")
	@Type(type = "pgsql_enum")
	private Status status;

	@Override
	public UserEventMessage parseEntity() {

		UserEvent userEvent = UserEvent.newBuilder()
				.setId(id)
				.setUserId(userId)
				.setEventId(eventId)
				.setTicket(ticket)
				.setStatus(status)
				.build();

		if (rating != null) {
			userEvent.toBuilder().setRating(Int64Value.of(rating)).build();
		}

		return new UserEventMessage(userEvent);
	}
}