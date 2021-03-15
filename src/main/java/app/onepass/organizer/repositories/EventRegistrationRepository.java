package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.onepass.organizer.entities.EventRegistrationEntity;

public interface EventRegistrationRepository extends JpaRepository<EventRegistrationEntity, Long> {

	long deleteByEventIdAndUserId(long eventId, long userId);
}
