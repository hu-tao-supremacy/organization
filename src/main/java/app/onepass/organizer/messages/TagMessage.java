package app.onepass.organizer.messages;

import app.onepass.apis.Tag;
import app.onepass.organizer.entities.TagEntity;
import lombok.Getter;

public class TagMessage implements BaseMessage<TagMessage, TagEntity> {

	@Getter
	Tag tag;

	public TagMessage(Tag tag) {
		this.tag = tag;
	}

	@Override
	public TagEntity parseMessage() {

		return TagEntity.builder().id(tag.getId()).name(tag.getName()).build();
	}
}
