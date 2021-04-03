package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import app.onepass.apis.AnswerType;
import app.onepass.apis.Question;
import app.onepass.organizer.messages.QuestionMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "question")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEntity implements BaseEntity<QuestionMessage, QuestionEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long questionGroupId;
	private long seq;
	@NotNull
	@Enumerated(EnumType.STRING)
	private AnswerType answerType;
	private boolean isOptional;
	@NotNull
	private String title;
	@NotNull
	private String subtitle;

	@Override
	public QuestionMessage parseEntity() {

		Question question = Question.newBuilder()
				.setId(id)
				.setQuestionGroupId(questionGroupId)
				.setSeq(seq)
				.setAnswerType(answerType)
				.setIsOptional(isOptional)
				.setTitle(title)
				.setSubtitle(subtitle)
				.build();

		return new QuestionMessage(question);
	}
}