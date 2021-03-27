package app.onepass.organizer.messages;

import app.onepass.apis.UserEvent;
import app.onepass.organizer.entities.UserEventEntity;
import lombok.Getter;

public class UserEventMessage implements BaseMessage<UserEventMessage, UserEventEntity>{

	public UserEventMessage(UserEvent userEvent) { this.userEvent = userEvent; }

	@Getter
	UserEvent userEvent;

	@Override
	public UserEventEntity parseMessage() {

		return UserEventEntity.builder()
				.id(userEvent.getId())
				.userId(userEvent.getUserId())
				.eventId(userEvent.getEventId())
				.rating(userEvent.getRating().getValue())
				.ticket(userEvent.getTicket())
				.status(userEvent.getStatus().toString())
				.build();
	}
}
