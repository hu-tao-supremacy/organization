package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.onepass.organizer.entities.LocationEntity;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {
}
