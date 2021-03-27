package app.onepass.organizer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.Empty;

import app.onepass.apis.GetByIdRequest;
import app.onepass.apis.GetQuestionGroupsByEventIdResponse;
import app.onepass.apis.GetQuestionsByGroupIdResponse;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.Question;
import app.onepass.apis.QuestionGroup;
import app.onepass.apis.QuestionGroupsRequest;
import app.onepass.apis.QuestionsRequest;
import app.onepass.organizer.entities.QuestionEntity;
import app.onepass.organizer.entities.QuestionGroupEntity;
import app.onepass.organizer.messages.QuestionGroupMessage;
import app.onepass.organizer.messages.QuestionMessage;
import app.onepass.organizer.repositories.QuestionGroupRepository;
import app.onepass.organizer.repositories.QuestionRepository;
import app.onepass.organizer.utilities.ServiceUtil;
import io.grpc.stub.StreamObserver;

@Service
public class QuestionService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	QuestionGroupRepository questionGroupRepository;

	@Override
	public void getQuestionGroupsByEventId(GetByIdRequest request, StreamObserver<GetQuestionGroupsByEventIdResponse> responseObserver) {

		List<QuestionGroupEntity> allQuestionGroupEntities = questionGroupRepository.findAllByEventId(request.getId());

		List<QuestionGroup> allQuestionGroups = allQuestionGroupEntities.stream()
				.map(questionGroupEntity -> questionGroupEntity.parseEntity().getQuestionGroup())
				.collect(Collectors.toList());

		GetQuestionGroupsByEventIdResponse getQuestionGroupResult = GetQuestionGroupsByEventIdResponse.newBuilder()
				.addAllQuestionGroup(allQuestionGroups).build();

		ServiceUtil.returnObject(responseObserver, getQuestionGroupResult);
	}

	@Override
	public void addQuestionGroups(QuestionGroupsRequest request, StreamObserver<Empty> responseObserver) {

		List<QuestionGroupEntity> questionGroupEntities = new ArrayList<>();

		for (int index = 0; index < request.getQuestionGroupsCount(); index++) {

			QuestionGroupMessage questionGroupMessage = new QuestionGroupMessage(request.getQuestionGroups(index));

			questionGroupEntities.add(questionGroupMessage.parseMessage());
		}

		questionGroupRepository.saveAll(questionGroupEntities);

		ServiceUtil.returnEmpty(responseObserver);
	}

	@Override
	public void removeQuestionGroups(QuestionGroupsRequest request, StreamObserver<Empty> responseObserver) {

		List<QuestionGroup> questionGroups = request.getQuestionGroupsList();

		List<QuestionGroupEntity> questionGroupEntities = questionGroupRepository.findAll();

		List<QuestionGroupEntity> entitiesToDelete = new ArrayList<>();

		//TODO: Optimize!

		for (QuestionGroupEntity questionGroupEntity : questionGroupEntities) {

			for (QuestionGroup questionGroup : questionGroups) {

				QuestionGroupEntity entity = (new QuestionGroupMessage(questionGroup)).parseMessage();

				if (questionGroupEntity.equals(entity)) {

					entitiesToDelete.add(questionGroupEntity);
				}
			}
		}

		questionGroupRepository.deleteAll(entitiesToDelete);

		ServiceUtil.returnEmpty(responseObserver);
	}

	@Override
	public void getQuestionsByGroupId(GetByIdRequest request, StreamObserver<GetQuestionsByGroupIdResponse> responseObserver) {

		List<QuestionEntity> allQuestionEntities = questionRepository.findAllByQuestionGroupId(request.getId());

		List<Question> allQuestions = allQuestionEntities.stream()
				.map(questionEntity -> questionEntity.parseEntity().getQuestion())
				.collect(Collectors.toList());

		GetQuestionsByGroupIdResponse getQuestionResult = GetQuestionsByGroupIdResponse.newBuilder()
				.addAllQuestion(allQuestions).build();

		ServiceUtil.returnObject(responseObserver, getQuestionResult);
	}

	@Override
	public void addQuestions(QuestionsRequest request, StreamObserver<Empty> responseObserver) {

		List<QuestionEntity> questionEntities = new ArrayList<>();

		for (int index = 0; index < request.getQuestionsCount(); index++) {

			QuestionMessage questionMessage = new QuestionMessage(request.getQuestions(index));

			questionEntities.add(questionMessage.parseMessage());
		}

		questionRepository.saveAll(questionEntities);

		ServiceUtil.returnEmpty(responseObserver);
	}

	@Override
	public void removeQuestions(QuestionsRequest request, StreamObserver<Empty> responseObserver) {

		List<Question> questions = request.getQuestionsList();

		List<QuestionEntity> questionEntities = questionRepository.findAll();

		List<QuestionEntity> entitiesToDelete = new ArrayList<>();

		//TODO: Optimize!

		for (QuestionEntity questionEntity : questionEntities) {

			for (Question question : questions) {

				QuestionEntity entity = (new QuestionMessage(question)).parseMessage();

				if (questionEntity.equals(entity)) {

					entitiesToDelete.add(questionEntity);
				}
			}
		}

		questionRepository.deleteAll(entitiesToDelete);

		ServiceUtil.returnEmpty(responseObserver);
	}
}