package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import app.onepass.apis.EventDuration;
import app.onepass.organizer.messages.EventDurationMessage;
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
	private long id;
	@NotNull
	private long eventId;
	@NotNull
	private com.google.protobuf.Timestamp start;
	@NotNull
	private java.sql.Timestamp finish;

	@Override
	public EventDurationMessage parseEntity() {

		com.google.protobuf.Timestamp start = com.google.protobuf.Timestamp.newBuilder()
				.setSeconds(this.getStart().getTime())
				.setNanos(this.getStart().getNanos())
				.build();

		com.google.protobuf.Timestamp finish = com.google.protobuf.Timestamp.newBuilder()
				.setSeconds(this.getFinish().getTime())
				.setNanos(this.getFinish().getNanos())
				.build();

		EventDuration eventDuration = EventDuration.newBuilder()
				.setId(this.getId())
				.setEventId(this.getEventId())
				.setStart(start)
				.setFinish(finish)
				.build();

		return new EventDurationMessage(eventDuration);
	}
}
