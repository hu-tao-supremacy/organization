package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.google.protobuf.MessageOrBuilder;

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
public class TagEntity implements BaseEntity {

	@Id
	@GeneratedValue
	private long id;
	@NotNull
	private String name;

	//TODO: Implement methods
	@Override
	public BaseEntity parseInto(MessageOrBuilder message) {
		return null;
	}

	@Override
	public MessageOrBuilder parseAway(BaseEntity entity) {
		return null;
	}
}
