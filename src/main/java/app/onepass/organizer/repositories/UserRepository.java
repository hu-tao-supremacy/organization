package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.onepass.organizer.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
