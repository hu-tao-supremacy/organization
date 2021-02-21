package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.onepass.organizer.entities.TagEntity;

@Repository
public interface TagRepository  extends JpaRepository<TagEntity, Long> {
}
