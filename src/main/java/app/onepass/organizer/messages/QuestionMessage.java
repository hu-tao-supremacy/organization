package app.onepass.organizer.messages;

import app.onepass.apis.Question;
import app.onepass.apis.Tag;
import app.onepass.organizer.entities.QuestionEntity;
import app.onepass.organizer.entities.TagEntity;
import lombok.Getter;

public class QuestionMessage implements BaseMessage<QuestionMessage, QuestionEntity> {

	public QuestionMessage(Question question) {
		this.question = question;
	}

	@Getter
	Question question;

	@Override
	public QuestionEntity parseMessage() {

		return QuestionEntity.builder()
				.id(question.getId())
				.questionGroupId(question.getQuestionGroupId())
				.order(question.getOrder())
				.answerType(question.getAnswerType().toString())
				.isOptional(question.getIsOptional())
				.title(question.getTitle())
				.subtitle(question.getSubtitle())
				.build();
	}
}