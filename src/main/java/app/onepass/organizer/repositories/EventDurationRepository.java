package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.onepass.organizer.entities.EventDurationEntity;

public interface EventDurationRepository extends JpaRepository<EventDurationEntity, Long> {

	long deleteAllByEventId(long eventId);
}
