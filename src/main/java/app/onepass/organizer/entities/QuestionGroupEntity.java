package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import app.onepass.apis.QuestionGroup;
import app.onepass.organizer.messages.QuestionGroupMessage;
import app.onepass.organizer.utilities.TypeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "question_group")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionGroupEntity implements BaseEntity<QuestionGroupMessage, QuestionGroupEntity> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int eventId;
	@NotNull
	private String type;
	private int seq;
	@NotNull
	private String title;

	@Override
	public QuestionGroupMessage parseEntity() {

		QuestionGroup questionGroup = QuestionGroup.newBuilder()
				.setId(id)
				.setEventId(eventId)
				.setType(TypeUtil.toQuestionGroupType(type))
				.setSeq(seq)
				.setTitle(title)
				.build();

		return new QuestionGroupMessage(questionGroup);
	}
}