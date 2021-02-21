package app.onepass.organizer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "tag")
@Getter
public class TagEntity {

	@Id
	@GeneratedValue
	private long id;
	private String name;
}
