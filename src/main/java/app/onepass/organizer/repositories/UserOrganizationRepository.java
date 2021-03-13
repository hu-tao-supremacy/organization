package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.onepass.organizer.entities.UserOrganizationEntity;

public interface UserOrganizationRepository  extends JpaRepository<UserOrganizationEntity, Long> { }
