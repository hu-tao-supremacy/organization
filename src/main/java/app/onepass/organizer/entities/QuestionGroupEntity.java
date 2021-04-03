package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import app.onepass.apis.QuestionGroup;
import app.onepass.apis.QuestionGroupType;
import app.onepass.organizer.messages.QuestionGroupMessage;
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
	private long id;
	private long eventId;
	@NotNull
	@Enumerated(EnumType.STRING)
	private QuestionGroupType type;
	private long seq;
	@NotNull
	private String title;

	@Override
	public QuestionGroupMessage parseEntity() {

		QuestionGroup questionGroup = QuestionGroup.newBuilder()
				.setId(id)
				.setEventId(eventId)
				.setType(type)
				.setSeq(seq)
				.setTitle(title)
				.build();

		return new QuestionGroupMessage(questionGroup);
	}
}