package app.onepass.organizer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.onepass.organizer.entities.EventTagEntity;

public interface EventTagRepository extends JpaRepository<EventTagEntity, Long> {

	List<EventTagEntity> findByEventId(long eventId);
}
