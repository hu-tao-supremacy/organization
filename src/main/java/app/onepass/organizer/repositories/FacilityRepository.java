package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.onepass.organizer.entities.FacilityEntity;

@Repository
public interface FacilityRepository  extends JpaRepository<FacilityEntity, Long> {
}
