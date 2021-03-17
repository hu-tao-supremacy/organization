package app.onepass.organizer.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.onepass.organizer.entities.EventRegistrationEntity;

public interface EventRegistrationRepository extends JpaRepository<EventRegistrationEntity, Long> {

	Optional<EventRegistrationEntity> findByEventIdAndUserId(long eventId, long userId);
}
