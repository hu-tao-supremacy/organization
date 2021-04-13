package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.onepass.organizer.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
