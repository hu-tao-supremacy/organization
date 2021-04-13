package app.onepass.organizer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.onepass.apis.AddQuestionGroupsRequest;
import app.onepass.apis.AddQuestionsRequest;
import app.onepass.apis.Answer;
import app.onepass.apis.AnswerListResponse;
import app.onepass.apis.GetObjectByIdRequest;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.Permission;
import app.onepass.apis.Question;
import app.onepass.apis.QuestionGroup;
import app.onepass.apis.QuestionGroupListResponse;
import app.onepass.apis.QuestionListResponse;
import app.onepass.apis.RemoveQuestionGroupsRequest;
import app.onepass.apis.RemoveQuestionsRequest;
import app.onepass.organizer.entities.AnswerEntity;
import app.onepass.organizer.entities.QuestionEntity;
import app.onepass.organizer.entities.QuestionGroupEntity;
import app.onepass.organizer.messages.QuestionGroupMessage;
import app.onepass.organizer.messages.QuestionMessage;
import app.onepass.organizer.repositories.AnswerRepository;
import app.onepass.organizer.repositories.EventRepository;
import app.onepass.organizer.repositories.QuestionGroupRepository;
import app.onepass.organizer.repositories.QuestionRepository;
import app.onepass.organizer.utilities.ServiceUtil;
import io.grpc.stub.StreamObserver;

@Service
public class QuestionService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Autowired
	AccountService accountService;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private QuestionGroupRepository questionGroupRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Override
	public void addQuestionGroups(AddQuestionGroupsRequest request, StreamObserver<QuestionGroupListResponse> responseObserver) {

		if (request.getQuestionGroupsCount() == 0) {

			ServiceUtil.returnObject(responseObserver, QuestionGroupListResponse.newBuilder().build());

			return;
		}

		int eventId = request.getQuestionGroups(0).getEventId();

		if (!ServiceUtil.hasValidParameters(accountService, eventRepository, responseObserver, request.getUserId(), eventId,
				Permission.EVENT_UPDATE)) {

			return;
		}

		List<QuestionGroupEntity> entitiesToAdd = new ArrayList<>();

		for (int index = 0; index < request.getQuestionGroupsCount(); index++) {

			QuestionGroup questionGroup = request.getQuestionGroups(index);

			if (questionGroup.getEventId() != eventId) {

				ServiceUtil.returnInvalidArgumentError(responseObserver, "Cannot add question groups with different event IDs.");

				return;
			}

			QuestionGroupMessage questionGroupMessage = new QuestionGroupMessage(questionGroup);

			entitiesToAdd.add(questionGroupMessage.parseMessage());
		}

		List<QuestionGroupEntity> addedEntities = questionGroupRepository.saveAll(entitiesToAdd);

		List<QuestionGroup> questionGroups = addedEntities.stream()
				.map(eventTagEntity -> eventTagEntity.parseEntity().getQuestionGroup())
				.collect(Collectors.toList());

		QuestionGroupListResponse questionGroupListResponse = QuestionGroupListResponse.newBuilder()
				.addAllQuestionGroups(questionGroups)
				.build();

		ServiceUtil.returnObject(responseObserver, questionGroupListResponse);
	}

	@Override
	public void removeQuestionGroups(RemoveQuestionGroupsRequest request, StreamObserver<QuestionGroupListResponse> responseObserver) {

		if (request.getQuestionGroupIdsCount() == 0) {

			ServiceUtil.returnObject(responseObserver, QuestionGroupListResponse.newBuilder().build());

			return;
		}

		int firstQuestionGroupId = request.getQuestionGroupIds(0);

		if (!hasValidEventId(responseObserver, firstQuestionGroupId, request.getUserId())) {

			return;
		}

		QuestionGroupEntity firstQuestionGroupEntity = questionGroupRepository.findById(firstQuestionGroupId)
				.orElseThrow(IllegalArgumentException::new);

		int eventId = firstQuestionGroupEntity.getEventId();

		List<Integer> questionGroupIds = request.getQuestionGroupIdsList();

		List<QuestionGroupEntity> entitiesToDelete = new ArrayList<>();

		for (int questionGroupId : questionGroupIds) {

			Optional<QuestionGroupEntity> questionGroupEntity = questionGroupRepository.findById(questionGroupId);

			if (questionGroupEntity.isPresent()) {

				if (questionGroupEntity.get().getEventId() != eventId) {

					ServiceUtil.returnInvalidArgumentError(responseObserver,
							"Cannot delete question groups with different event ID.");

					return;
				}

				entitiesToDelete.add(questionGroupEntity.get());
			}

		}

		questionGroupRepository.deleteAll(entitiesToDelete);

		List<QuestionGroup> questionGroups = entitiesToDelete.stream()
				.map(eventTagEntity -> eventTagEntity.parseEntity().getQuestionGroup())
				.collect(Collectors.toList());

		QuestionGroupListResponse questionGroupListResponse = QuestionGroupListResponse.newBuilder()
				.addAllQuestionGroups(questionGroups)
				.build();

		ServiceUtil.returnObject(responseObserver, questionGroupListResponse);
	}

	@Override
	public void addQuestions(AddQuestionsRequest request, StreamObserver<QuestionListResponse> responseObserver) {

		if (request.getQuestionsCount() == 0) {

			ServiceUtil.returnObject(responseObserver, QuestionListResponse.newBuilder().build());

			return;
		}

		int questionGroupId = request.getQuestions(0).getQuestionGroupId();

		if (!hasValidEventId(responseObserver, questionGroupId, request.getUserId())) {
			return;
		}

		List<QuestionEntity> entitiesToAdd = new ArrayList<>();

		for (int index = 0; index < request.getQuestionsCount(); index++) {

			QuestionMessage questionMessage = new QuestionMessage(request.getQuestions(index));

			entitiesToAdd.add(questionMessage.parseMessage());
		}

		List<QuestionEntity> addedEntities = questionRepository.saveAll(entitiesToAdd);

		List<Question> questions = addedEntities.stream()
				.map(eventTagEntity -> eventTagEntity.parseEntity().getQuestion())
				.collect(Collectors.toList());

		QuestionListResponse questionListResponse = QuestionListResponse.newBuilder()
				.addAllQuestions(questions)
				.build();

		ServiceUtil.returnObject(responseObserver, questionListResponse);
	}

	@Override
	public void removeQuestions(RemoveQuestionsRequest request, StreamObserver<QuestionListResponse> responseObserver) {

		if (request.getQuestionIdsCount() == 0) {

			ServiceUtil.returnObject(responseObserver, QuestionListResponse.newBuilder().build());

			return;
		}

		int firstQuestionId = request.getQuestionIds(0);

		int questionGroupId;

		try {

			QuestionEntity questionEntity = questionRepository.findById(firstQuestionId)
					.orElseThrow(IllegalArgumentException::new);

			questionGroupId = questionEntity.getQuestionGroupId();

		} catch (IllegalArgumentException exception) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "The first question ID does not exist.");

			return;
		}

		if (!hasValidEventId(responseObserver, questionGroupId, request.getUserId())) {
			return;
		}

		List<Integer> questionIds = request.getQuestionIdsList();

		List<QuestionEntity> entitiesToDelete = new ArrayList<>();

		for (int questionId : questionIds) {

			Optional<QuestionEntity> questionEntity = questionRepository.findById(questionId);

			if (questionEntity.isPresent()) {

				if (questionEntity.get().getQuestionGroupId() != questionGroupId) {

					ServiceUtil.returnInvalidArgumentError(responseObserver,
							"Cannot delete questions with different question group ID.");

					return;
				}

				entitiesToDelete.add(questionEntity.get());
			}

		}

		questionRepository.deleteAll(entitiesToDelete);

		List<Question> questions = entitiesToDelete.stream()
				.map(eventTagEntity -> eventTagEntity.parseEntity().getQuestion())
				.collect(Collectors.toList());

		QuestionListResponse questionListResponse = QuestionListResponse.newBuilder()
				.addAllQuestions(questions)
				.build();

		ServiceUtil.returnObject(responseObserver, questionListResponse);
	}

	@Override
	public void getAnswersByQuestionId(GetObjectByIdRequest request, StreamObserver<AnswerListResponse> responseObserver) {

		List<AnswerEntity> answerEntities = answerRepository.findAllByQuestionId(request.getId());

		List<Answer> answers = answerEntities.stream()
				.map(answerEntity -> answerEntity.parseEntity().getAnswer())
				.collect(Collectors.toList());

		AnswerListResponse answerListResponse = AnswerListResponse.newBuilder()
				.addAllAnswers(answers)
				.build();

		ServiceUtil.returnObject(responseObserver, answerListResponse);
	}

	private <T> boolean hasValidEventId(StreamObserver<T> responseObserver, int questionGroupId, int userId) {

		int eventId;

		try {

			QuestionGroupEntity questionGroupEntity = questionGroupRepository.findById(questionGroupId)
					.orElseThrow(IllegalArgumentException::new);

			eventId = questionGroupEntity.getEventId();

		} catch (IllegalArgumentException exception) {

			ServiceUtil.returnInvalidArgumentError(responseObserver,
					"The first question ID does not refer to the existing question groups.");

			return false;
		}

		return ServiceUtil.hasValidParameters(accountService, eventRepository, responseObserver, userId, eventId,
				Permission.EVENT_UPDATE);
	}
}