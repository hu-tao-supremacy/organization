package app.onepass.organizer.messages;

import app.onepass.apis.Answer;
import app.onepass.organizer.entities.AnswerEntity;
import lombok.Getter;

public class AnswerMessage implements BaseMessage<AnswerMessage, AnswerEntity> {

	@Getter
	Answer answer;

	public AnswerMessage(Answer answer) {
		this.answer = answer;
	}

	@Override
	public AnswerEntity parseMessage() {

		return AnswerEntity.builder()
				.id(answer.getId())
				.userEventId(answer.getUserEventId())
				.questionId(answer.getQuestionId())
				.value(answer.getValue())
				.build();
	}
}
