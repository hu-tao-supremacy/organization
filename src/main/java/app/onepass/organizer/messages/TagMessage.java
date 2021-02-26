package app.onepass.organizer.messages;

import app.onepass.organizer.entities.TagEntity;

public class TagMessage implements BaseMessage<TagMessage, TagEntity>{

	@Override
	public TagEntity parseMessage() {
		return null;
	}
}
