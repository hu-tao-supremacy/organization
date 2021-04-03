package app.onepass.organizer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.Empty;

import app.onepass.apis.AddQuestionGroupsRequest;
import app.onepass.apis.AddQuestionsRequest;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.Permission;
import app.onepass.apis.QuestionGroup;
import app.onepass.apis.RemoveQuestionGroupsRequest;
import app.onepass.apis.RemoveQuestionsRequest;
import app.onepass.organizer.entities.QuestionEntity;
import app.onepass.organizer.entities.QuestionGroupEntity;
import app.onepass.organizer.messages.QuestionGroupMessage;
import app.onepass.organizer.messages.QuestionMessage;
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

	@Override
	public void addQuestionGroups(AddQuestionGroupsRequest request, StreamObserver<Empty> responseObserver) {

		if (request.getQuestionGroupsCount() == 0) {

			ServiceUtil.returnEmpty(responseObserver);

			return;
		}

		long eventId = request.getQuestionGroups(0).getEventId();

		if (!ServiceUtil.hasValidParameters(accountService, eventRepository, responseObserver, request.getUserId(), eventId,
				Permission.EVENT_UPDATE)) {

			return;
		}

		List<QuestionGroupEntity> questionGroupEntities = new ArrayList<>();

		for (int index = 0; index < request.getQuestionGroupsCount(); index++) {

			QuestionGroup questionGroup = request.getQuestionGroups(index);

			if (questionGroup.getEventId() != eventId) {

				ServiceUtil.returnInvalidArgumentError(responseObserver, "Cannot add question groups with different event IDs.");

				return;
			}

			QuestionGroupMessage questionGroupMessage = new QuestionGroupMessage(questionGroup);

			questionGroupEntities.add(questionGroupMessage.parseMessage());
		}

		questionGroupRepository.saveAll(questionGroupEntities);

		ServiceUtil.returnEmpty(responseObserver);
	}

	@Override
	public void removeQuestionGroups(RemoveQuestionGroupsRequest request, StreamObserver<Empty> responseObserver) {

		if (request.getQuestionGroupIdsCount() == 0) {

			ServiceUtil.returnEmpty(responseObserver);

			return;
		}

		long firstQuestionGroupId = request.getQuestionGroupIds(0);

		long eventId;

		try {

			QuestionGroupEntity questionGroupEntity = questionGroupRepository.findById(firstQuestionGroupId)
					.orElseThrow(IllegalArgumentException::new);

			eventId = questionGroupEntity.getEventId();

		} catch (IllegalArgumentException exception) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "The first question group ID does not exist.");

			return;
		}

		if (!ServiceUtil.hasValidParameters(accountService, eventRepository, responseObserver, request.getUserId(), eventId,
				Permission.EVENT_UPDATE)) {

			return;
		}

		List<Long> questionGroupIds = request.getQuestionGroupIdsList();

		List<QuestionGroupEntity> entitiesToDelete = new ArrayList<>();

		for (long questionGroupId : questionGroupIds) {

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

		ServiceUtil.returnEmpty(responseObserver);
	}

	@Override
	public void addQuestions(AddQuestionsRequest request, StreamObserver<Empty> responseObserver) {

		if (request.getQuestionsCount() == 0) {

			ServiceUtil.returnEmpty(responseObserver);

			return;
		}

		long eventId = request.getQuestions(0).getQuestionGroupId();

		if (!ServiceUtil.hasValidParameters(accountService, eventRepository, responseObserver, request.getUserId(), eventId,
				Permission.EVENT_UPDATE)) {

			return;
		}

		List<QuestionEntity> questionEntities = new ArrayList<>();

		for (int index = 0; index < request.getQuestionsCount(); index++) {

			QuestionMessage questionMessage = new QuestionMessage(request.getQuestions(index));

			questionEntities.add(questionMessage.parseMessage());
		}

		questionRepository.saveAll(questionEntities);

		ServiceUtil.returnEmpty(responseObserver);
	}

	@Override
	public void removeQuestions(RemoveQuestionsRequest request, StreamObserver<Empty> responseObserver) {

		if (request.getQuestionIdsCount() == 0) {

			ServiceUtil.returnEmpty(responseObserver);

			return;
		}

		long firstQuestionId = request.getQuestionIds(0);

		long questionGroupId;

		try {

			QuestionEntity questionEntity = questionRepository.findById(firstQuestionId)
					.orElseThrow(IllegalArgumentException::new);

			questionGroupId = questionEntity.getQuestionGroupId();

		} catch (IllegalArgumentException exception) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "The first question ID does not exist.");

			return;
		}

		long eventId;

		try {

			QuestionGroupEntity questionGroupEntity = questionGroupRepository.findById(questionGroupId)
					.orElseThrow(IllegalArgumentException::new);

			eventId = questionGroupEntity.getEventId();

		} catch (IllegalArgumentException exception) {

			ServiceUtil.returnInvalidArgumentError(responseObserver,
					"The first question ID does not refer to the existing question groups.");

			return;
		}

		if (!ServiceUtil.hasValidParameters(accountService, eventRepository, responseObserver, request.getUserId(), eventId,
				Permission.EVENT_UPDATE)) {

			return;
		}

		List<Long> questionIds = request.getQuestionIdsList();

		List<QuestionEntity> entitiesToDelete = new ArrayList<>();

		for (long questionId : questionIds) {

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

		ServiceUtil.returnEmpty(responseObserver);
	}
}