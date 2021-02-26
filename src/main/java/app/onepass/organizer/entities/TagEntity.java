package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import app.onepass.organizer.messages.TagMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
		name = "tag",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})}
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagEntity implements BaseEntity<TagMessage, TagEntity> {

	@Id
	@GeneratedValue
	private long id;
	@NotNull
	private String name;

	@Override
	public TagMessage parseEntity() {
		return null;
	}
}
