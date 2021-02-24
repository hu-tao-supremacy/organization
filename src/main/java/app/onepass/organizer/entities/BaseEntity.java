package app.onepass.organizer.entities;

import com.google.protobuf.MessageOrBuilder;

public interface BaseEntity<T extends MessageOrBuilder, S extends BaseEntity<T, S>> {

	public S parseInto(T message);

	public T parseAway(S entity);
}
