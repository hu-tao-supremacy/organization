package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import app.onepass.apis.EventTag;
import app.onepass.organizer.messages.EventTagMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event_tag")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventTagEntity implements BaseEntity<EventTagMessage, EventTagEntity> {

	@Id
	@GeneratedValue
	private long id;
	@NotNull
	private long eventId;
	@NotNull
	private long tagId;

	@Override
	public EventTagMessage parseEntity() {

		EventTag eventTag = EventTag.newBuilder()
				.setId(id)
				.setEventId(eventId)
				.setTagId(tagId)
				.build();

		return new EventTagMessage(eventTag);
	}
}