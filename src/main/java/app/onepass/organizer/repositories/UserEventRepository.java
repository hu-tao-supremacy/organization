package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.onepass.organizer.entities.UserEventEntity;

@Repository
public interface UserEventRepository extends JpaRepository<UserEventEntity, Integer> {

	UserEventEntity findByUserIdAndEventId(int userId, int eventId);

	UserEventEntity findByTicketAndEventId(String ticket, int eventId);
}
