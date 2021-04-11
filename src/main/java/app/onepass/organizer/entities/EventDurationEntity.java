package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import app.onepass.apis.EventDuration;
import app.onepass.organizer.messages.EventDurationMessage;
import app.onepass.organizer.utilities.TypeUtil;
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
public class EventDurationEntity implements BaseEntity<EventDurationMessage, EventDurationEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int eventId;
	@NotNull
	private java.sql.Timestamp start;
	@NotNull
	private java.sql.Timestamp finish;

	@Override
	public EventDurationMessage parseEntity() {

		com.google.protobuf.Timestamp startTime = TypeUtil.toProtobufTimestamp(start);
		com.google.protobuf.Timestamp finishTime = TypeUtil.toProtobufTimestamp(finish);

		EventDuration eventDuration = EventDuration.newBuilder()
				.setId(id)
				.setEventId(eventId)
				.setStart(startTime)
				.setFinish(finishTime)
				.build();

		return new EventDurationMessage(eventDuration);
	}
}
