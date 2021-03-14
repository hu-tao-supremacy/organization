package app.onepass.organizer.services;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.Empty;

import app.onepass.apis.CreateEventRequest;
import app.onepass.apis.CreateOrganizationRequest;
import app.onepass.apis.GetByIdRequest;
import app.onepass.apis.GetEventByIdResponse;
import app.onepass.apis.GetEventResponse;
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
import app.onepass.apis.UpdateEventFacilityRequest;
import app.onepass.apis.UpdateEventInfoRequest;
import app.onepass.apis.UpdateOrganizationRequest;
import app.onepass.apis.UpdateRegistrationRequestRequest;
import app.onepass.apis.UpdateTagRequest;
import app.onepass.apis.UpdateUsersInOrganizationRequest;
import app.onepass.apis.UserRequest;
import io.grpc.stub.StreamObserver;

@GRpcService
public class BaseService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Autowired
	EventService eventService;

	@Autowired
	OrganizationService organizationService;

	@Autowired
	PingService pingService;

	@Override
	public void createOrganization(CreateOrganizationRequest request, StreamObserver<Result> responseObserver) {
		organizationService.createOrganization(request, responseObserver);
	}

	@Override
	public void getOrganization(UserRequest request, StreamObserver<GetOrganizationResponse> responseObserver) {
		organizationService.getOrganization(request, responseObserver);
	}

	@Override
	public void getOrganizationById(GetByIdRequest request, StreamObserver<GetOrganizationByIdResponse> responseObserver) {
		organizationService.getOrganizationById(request, responseObserver);
	}

	@Override
	public void updateOrganization(UpdateOrganizationRequest request, StreamObserver<Result> responseObserver) {
		organizationService.updateOrganization(request, responseObserver);
	}

	@Override
	public void removeOrganization(RemoveOrganizationRequest request, StreamObserver<Result> responseObserver) {
		organizationService.removeOrganization(request, responseObserver);
	}

	@Override
	public void addUsersToOrganization(UpdateUsersInOrganizationRequest request, StreamObserver<Result> responseObserver) {
		super.addUsersToOrganization(request, responseObserver);
	}

	@Override
	public void removeUsersFromOrganization(UpdateUsersInOrganizationRequest request, StreamObserver<Result> responseObserver) {
		super.removeUsersFromOrganization(request, responseObserver);
	}

	@Override
	public void createEvent(CreateEventRequest request, StreamObserver<Result> responseObserver) {
		eventService.createEvent(request, responseObserver);
	}

	@Override
	public void getEvent(UserRequest request, StreamObserver<GetEventResponse> responseObserver) {
		eventService.getEvent(request, responseObserver);
	}

	@Override
	public void getEventById(GetByIdRequest request, StreamObserver<GetEventByIdResponse> responseObserver) {
		eventService.getEventById(request, responseObserver);
	}

	@Override
	public void updateEventInfo(UpdateEventInfoRequest request, StreamObserver<Result> responseObserver) {
		eventService.updateEventInfo(request, responseObserver);
	}

	@Override
	public void updateEventFacility(UpdateEventFacilityRequest request, StreamObserver<Result> responseObserver) {
		super.updateEventFacility(request, responseObserver);
	}

	@Override
	public void updateEventDuration(UpdateEventDurationRequest request, StreamObserver<Result> responseObserver) {
		super.updateEventDuration(request, responseObserver);
	}

	@Override
	public void removeEvent(RemoveEventRequest request, StreamObserver<Result> responseObserver) {
		eventService.removeEvent(request, responseObserver);
	}

	@Override
	public void updateRegistrationRequest(UpdateRegistrationRequestRequest request, StreamObserver<Result> responseObserver) {
		super.updateRegistrationRequest(request, responseObserver);
	}

	@Override
	public void addTag(UpdateTagRequest request, StreamObserver<Result> responseObserver) {
		super.addTag(request, responseObserver);
	}

	@Override
	public void removeTag(UpdateTagRequest request, StreamObserver<Result> responseObserver) {
		super.removeTag(request, responseObserver);
	}

	@Override
	public void getTag(UserRequest request, StreamObserver<GetTagResponse> responseObserver) {
		super.getTag(request, responseObserver);
	}

	@Override
	public void getTagById(GetByIdRequest request, StreamObserver<GetTagByIdResponse> responseObserver) {
		super.getTagById(request, responseObserver);
	}

	@Override
	public void hasEvent(HasEventRequest request, StreamObserver<Result> responseObserver) {
		super.hasEvent(request, responseObserver);
	}

	@Override
	public void ping(Empty request, StreamObserver<Result> responseObserver) {
		pingService.ping(request, responseObserver);
	}
}

