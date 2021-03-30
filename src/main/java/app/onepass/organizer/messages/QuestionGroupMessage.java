package app.onepass.organizer.messages;

import app.onepass.apis.QuestionGroup;
import app.onepass.organizer.entities.QuestionGroupEntity;
import lombok.Getter;

public class QuestionGroupMessage implements BaseMessage<QuestionGroupMessage, QuestionGroupEntity> {

	@Getter
	QuestionGroup questionGroup;

	public QuestionGroupMessage(QuestionGroup questionGroup) {
		this.questionGroup = questionGroup;
	}

	@Override
	public QuestionGroupEntity parseMessage() {

		return QuestionGroupEntity.builder()
				.id(questionGroup.getId())
				.eventId(questionGroup.getEventId())
				.type(questionGroup.getType().toString())
				.seq(questionGroup.getSeq())
				.title(questionGroup.getTitle())
				.build();
	}
}