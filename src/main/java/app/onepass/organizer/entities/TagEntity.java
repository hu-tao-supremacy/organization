package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Entity
@Table(
		name = "tag",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})}
)
@Getter
public class TagEntity {

	@Id
	@GeneratedValue
	private long id;
	@NotNull
	private String name;
}
