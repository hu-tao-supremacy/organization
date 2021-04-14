package app.onepass.organizer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import app.onepass.organizer.entities.AnswerEntity;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Integer> {

	List<AnswerEntity> findAllByQuestionId(int questionId);
}
