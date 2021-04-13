package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import app.onepass.apis.Answer;
import app.onepass.organizer.messages.AnswerMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "answer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerEntity implements BaseEntity<AnswerMessage, AnswerEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int userEventId;
	private int questionId;
	@NotNull
	private String value;

	@Override
	public AnswerMessage parseEntity() {

		Answer answer = Answer.newBuilder()
				.setId(id)
				.setUserEventId(userEventId)
				.setQuestionId(questionId)
				.setValue(value)
				.build();

		return new AnswerMessage(answer);
	}
}
