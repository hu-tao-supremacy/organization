package app.onepass.organizer.messages;

import app.onepass.organizer.entities.BaseEntity;

public interface BaseMessage<M extends BaseMessage<M, E>, E extends BaseEntity<M, E>> {

	public E parseMessage();
}
