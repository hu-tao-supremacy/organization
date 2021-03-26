package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.onepass.organizer.entities.UserEventEntity;

@Repository
public interface UserEventRepository extends JpaRepository<UserEventEntity, Long> {

	UserEventEntity findByUserIdAndEventId(long userId, long eventId);
}
