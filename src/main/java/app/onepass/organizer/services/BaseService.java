package app.onepass.organizer.services;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.Empty;

import app.onepass.apis.CreateEventRequest;
import app.onepass.apis.CreateOrganizationRequest;
import app.onepass.apis.DeleteEventRequest;
import app.onepass.apis.DeleteOrganizationRequest;
import app.onepass.apis.HasEventRequest;
import app.onepass.apis.OrganizationServiceGrpc;
import app.onepass.apis.ReadByIdRequest;
import app.onepass.apis.ReadEventByIdResult;
import app.onepass.apis.ReadEventResult;
import app.onepass.apis.ReadOrganizationByIdResult;
import app.onepass.apis.ReadOrganizationResult;
import app.onepass.apis.ReadTagByIdResult;
import app.onepass.apis.ReadTagResult;
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
	OrganizationService organizationService;

	@Autowired
	PingService pingService;

	@Override
	public void createOrganization(CreateOrganizationRequest request, StreamObserver<Result> responseObserver) {
		organizationService.createOrganization(request, responseObserver);
	}

	@Override
	public void readOrganization(UserRequest request, StreamObserver<ReadOrganizationResult> responseObserver) {
		organizationService.readOrganization(request, responseObserver);
	}

	@Override
	public void readOrganizationById(ReadByIdRequest request, StreamObserver<ReadOrganizationByIdResult> responseObserver) {
		organizationService.readOrganizationById(request, responseObserver);
	}

	@Override
	public void updateOrganization(UpdateOrganizationRequest request, StreamObserver<Result> responseObserver) {
		organizationService.updateOrganization(request, responseObserver);
	}

	@Override
	public void deleteOrganization(DeleteOrganizationRequest request, StreamObserver<Result> responseObserver) {
		organizationService.deleteOrganization(request, responseObserver);
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
		super.createEvent(request, responseObserver);
	}

	@Override
	public void readEvent(UserRequest request, StreamObserver<ReadEventResult> responseObserver) {
		super.readEvent(request, responseObserver);
	}

	@Override
	public void readEventById(ReadByIdRequest request, StreamObserver<ReadEventByIdResult> responseObserver) {
		super.readEventById(request, responseObserver);
	}

	@Override
	public void updateEventInfo(UpdateEventInfoRequest request, StreamObserver<Result> responseObserver) {
		super.updateEventInfo(request, responseObserver);
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
	public void deleteEvent(DeleteEventRequest request, StreamObserver<Result> responseObserver) {
		super.deleteEvent(request, responseObserver);
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
	public void readTag(UserRequest request, StreamObserver<ReadTagResult> responseObserver) {
		super.readTag(request, responseObserver);
	}

	@Override
	public void readTagById(ReadByIdRequest request, StreamObserver<ReadTagByIdResult> responseObserver) {
		super.readTagById(request, responseObserver);
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

