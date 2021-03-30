package app.onepass.organizer.entities;

import app.onepass.organizer.messages.BaseMessage;

public interface BaseEntity<M extends BaseMessage<M, E>, E extends BaseEntity<M, E>> {

	M parseEntity();
}
