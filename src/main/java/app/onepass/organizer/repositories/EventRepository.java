package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.onepass.organizer.entities.EventEntity;

@Repository
public interface EventRepository  extends JpaRepository<EventEntity, Long> {
}
