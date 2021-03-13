package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.onepass.organizer.entities.EventTagEntity;

public interface EventTagRepository extends JpaRepository<EventTagEntity, Long> { }
