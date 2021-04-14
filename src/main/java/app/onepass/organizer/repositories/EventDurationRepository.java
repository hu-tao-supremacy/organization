package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import app.onepass.organizer.entities.EventDurationEntity;

@Repository
public interface EventDurationRepository extends JpaRepository<EventDurationEntity, Integer> {

	long deleteAllByEventId(int eventId);
}
