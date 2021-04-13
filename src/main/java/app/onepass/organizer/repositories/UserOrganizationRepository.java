package app.onepass.organizer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.onepass.organizer.entities.UserOrganizationEntity;

public interface UserOrganizationRepository extends JpaRepository<UserOrganizationEntity, Integer> {

	List<UserOrganizationEntity> findByOrganizationId(int organizationId);

	UserOrganizationEntity findByUserIdAndOrganizationId(int userId, int organizationId);
}
