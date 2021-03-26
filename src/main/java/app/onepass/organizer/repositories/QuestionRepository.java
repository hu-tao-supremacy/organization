package app.onepass.organizer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.onepass.organizer.entities.QuestionEntity;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

	List<QuestionEntity> findAllByQuestionGroupId(long questionGroupId);
}
