package app.onepass.organizer.services;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.protobuf.Empty;

import app.onepass.apis.CreateEventRequest;
import app.onepass.apis.CreateOrganizationRequest;
import app.onepass.apis.CreateTagRequest;
import app.onepass.apis.Event;
import app.onepass.apis.GetByIdRequest;
import app.onepass.apis.GetOrganizationByIdResponse;
import app.onepass.apis.GetOrganizationResponse;
import app.onepass.apis.GetTagByIdResponse;
import app.onepass.apis.GetTagResponse;
import app.onepass.apis.HasEventRequest;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.RemoveEventRequest;
import app.onepass.apis.RemoveOrganizationRequest;
import app.onepass.apis.Result;
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
	PingService pingService;

	@Override
	@Transactional
	public void createOrganization(CreateOrganizationRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(organizationService::createOrganization, request, responseObserver);
	}

	@Override
	public void getOrganization(Empty request, StreamObserver<GetOrganizationResponse> responseObserver) {
		organizationService.getOrganization(request, responseObserver);
	}

	@Override
	public void getOrganizationById(GetByIdRequest request, StreamObserver<GetOrganizationByIdResponse> responseObserver) {
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
	public void updateEventDuration(UpdateEventDurationRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(eventService::updateEventDuration, request, responseObserver);
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
	public void addTag(UpdateTagRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(tagService::addTag, request, responseObserver);
	}

	@Override
	@Transactional
	public void removeTag(UpdateTagRequest request, StreamObserver<Empty> responseObserver) {
		DatabaseExceptionCatcher.catcher(tagService::removeTag, request, responseObserver);
	}

	@Override
	public void getTag(Empty request, StreamObserver<GetTagResponse> responseObserver) {
		tagService.getTag(request, responseObserver);
	}

	@Override
	public void getTagById(GetByIdRequest request, StreamObserver<GetTagByIdResponse> responseObserver) {
		tagService.getTagById(request, responseObserver);
	}

	@Override
	public void hasEvent(HasEventRequest request, StreamObserver<Event> responseObserver) {
		eventService.hasEvent(request, responseObserver);
	}

	@Override
	public void ping(Empty request, StreamObserver<Result> responseObserver) {
		pingService.ping(request, responseObserver);
	}
}

