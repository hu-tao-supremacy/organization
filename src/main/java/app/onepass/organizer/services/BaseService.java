package app.onepass.organizer.services;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;

import app.onepass.apis.AddQuestionGroupsRequest;
import app.onepass.apis.AddQuestionsRequest;
import app.onepass.apis.CreateEventRequest;
import app.onepass.apis.CreateOrganizationRequest;
import app.onepass.apis.CreateTagRequest;
import app.onepass.apis.Event;
import app.onepass.apis.GetObjectByIdRequest;
import app.onepass.apis.GetOrganizationByIdResponse;
import app.onepass.apis.GetOrganizationsResponse;
import app.onepass.apis.GetQuestionGroupsByEventIdResponse;
import app.onepass.apis.GetQuestionsByGroupIdResponse;
import app.onepass.apis.GetTagByIdResponse;
import app.onepass.apis.GetTagsResponse;
import app.onepass.apis.HasEventRequest;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.RemoveEventRequest;
import app.onepass.apis.RemoveOrganizationRequest;
import app.onepass.apis.RemoveQuestionGroupsRequest;
import app.onepass.apis.RemoveQuestionsRequest;
import app.onepass.apis.UpdateEventDurationRequest;
import app.onepass.apis.UpdateEventRequest;
import app.onepass.apis.UpdateOrganizationRequest;
import app.onepass.apis.UpdateRegistrationRequestRequest;
import app.onepass.apis.UpdateTagRequest;
import app.onepass.apis.UpdateUsersInOrganizationRequest;
import app.onepass.organizer.utilities.DatabaseExceptionCatcher;
import io.grpc.stub.StreamObserver;

@GRpcService
public class BaseService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Autowired
	EventService eventService;

	@Autowired
	OrganizationService organizationService;

	@Autowired
	TagService tagService;

	@Autowired
	QuestionService questionService;

	@Autowired
	PingService pingService;

	@Override
	@Transactional
	public void createOrganization(CreateOrganizationRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(organizationService::createOrganization, request, responseObserver);
	}

	@Override
	public void getOrganizations(Empty request, StreamObserver<GetOrganizationsResponse> responseObserver) {
		organizationService.getOrganizations(request, responseObserver);
	}

	@Override
	public void getOrganizationById(GetObjectByIdRequest request, StreamObserver<GetOrganizationByIdResponse> responseObserver) {
		organizationService.getOrganizationById(request, responseObserver);
	}

	@Override
	@Transactional
	public void updateOrganization(UpdateOrganizationRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(organizationService::updateOrganization, request, responseObserver);
	}

	@Override
	@Transactional
	public void removeOrganization(RemoveOrganizationRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(organizationService::removeOrganization, request, responseObserver);
	}

	@Override
	@Transactional
	public void addUsersToOrganization(UpdateUsersInOrganizationRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(organizationService::addUsersToOrganization, request, responseObserver);
	}

	@Override
	@Transactional
	public void removeUsersFromOrganization(UpdateUsersInOrganizationRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(organizationService::removeUsersFromOrganization, request, responseObserver);
	}

	@Override
	@Transactional
	public void createEvent(CreateEventRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(eventService::createEvent, request, responseObserver);
	}

	@Override
	@Transactional
	public void updateEvent(UpdateEventRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(eventService::updateEvent, request, responseObserver);
	}

	@Override
	@Transactional
	public void updateEventDurations(UpdateEventDurationRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(eventService::updateEventDurations, request, responseObserver);
	}

	@Override
	@Transactional
	public void removeEvent(RemoveEventRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(eventService::removeEvent, request, responseObserver);
	}

	@Override
	public void updateRegistrationRequest(UpdateRegistrationRequestRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(eventService::updateRegistrationRequest, request, responseObserver);
	}

	@Override
	@Transactional
	public void createTag(CreateTagRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(tagService::createTag, request, responseObserver);
	}

	@Override
	public void addTags(UpdateTagRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(tagService::addTags, request, responseObserver);
	}

	@Override
	@Transactional
	public void removeTags(UpdateTagRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(tagService::removeTags, request, responseObserver);
	}

	@Override
	public void getTags(Empty request, StreamObserver<GetTagsResponse> responseObserver) {
		tagService.getTags(request, responseObserver);
	}

	@Override
	public void getTagById(GetObjectByIdRequest request, StreamObserver<GetTagByIdResponse> responseObserver) {
		tagService.getTagById(request, responseObserver);
	}

	@Override
	public void hasEvent(HasEventRequest request, StreamObserver<Event> responseObserver) {
		eventService.hasEvent(request, responseObserver);
	}

	@Override
	public void getQuestionGroupsByEventId(GetObjectByIdRequest request,
			StreamObserver<GetQuestionGroupsByEventIdResponse> responseObserver) {
		questionService.getQuestionGroupsByEventId(request, responseObserver);
	}

	@Override
	@Transactional
	public void addQuestionGroups(AddQuestionGroupsRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(questionService::addQuestionGroups, request, responseObserver);
	}

	@Override
	@Transactional
	public void removeQuestionGroups(RemoveQuestionGroupsRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(questionService::removeQuestionGroups, request, responseObserver);
	}

	@Override
	public void getQuestionsByGroupId(GetObjectByIdRequest request,
			StreamObserver<GetQuestionsByGroupIdResponse> responseObserver) {
		questionService.getQuestionsByGroupId(request, responseObserver);
	}

	@Override
	@Transactional
	public void addQuestions(AddQuestionsRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(questionService::addQuestions, request, responseObserver);
	}

	@Override
	@Transactional
	public void removeQuestions(RemoveQuestionsRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(questionService::removeQuestions, request, responseObserver);
	}

	@Override
	public void ping(Empty request, StreamObserver<BoolValue> responseObserver) {
		pingService.ping(request, responseObserver);
	}
}

