package app.onepass.organizer.messages;

import app.onepass.apis.Question;
import app.onepass.organizer.entities.QuestionEntity;
import lombok.Getter;

public class QuestionMessage implements BaseMessage<QuestionMessage, QuestionEntity> {

	@Getter
	Question question;

	public QuestionMessage(Question question) {
		this.question = question;
	}

	@Override
	public QuestionEntity parseMessage() {

		return QuestionEntity.builder()
				.id(question.getId())
				.questionGroupId(question.getQuestionGroupId())
				.seq(question.getSeq())
				.answerType(question.getAnswerType())
				.isOptional(question.getIsOptional())
				.title(question.getTitle())
				.subtitle(question.getSubtitle())
				.build();
	}
}