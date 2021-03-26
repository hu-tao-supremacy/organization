package app.onepass.organizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.onepass.organizer.entities.QuestionGroupEntity;

@Repository
public interface QuestionGroupRepository extends JpaRepository<QuestionGroupEntity, Long> {}
