package app.onepass.organizer.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.onepass.apis.CreateEventRequest;
import app.onepass.apis.DeleteEventRequest;
import app.onepass.apis.Event;
import app.onepass.apis.HasEventRequest;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.ReadByIdRequest;
import app.onepass.apis.ReadEventByIdResult;
import app.onepass.apis.ReadEventResult;
import app.onepass.apis.Result;
import app.onepass.apis.UpdateEventDurationRequest;
import app.onepass.apis.UpdateEventFacilityRequest;
import app.onepass.apis.UpdateEventInfoRequest;
import app.onepass.apis.UpdateRegistrationRequestRequest;
import app.onepass.apis.UserRequest;
import app.onepass.organizer.entities.EventEntity;
import app.onepass.organizer.messages.EventMessage;
import app.onepass.organizer.repositories.EventRepository;
import app.onepass.organizer.utilities.ServiceUtil;
import io.grpc.stub.StreamObserver;

@Service
public class EventService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Autowired
	private EventRepository eventRepository;

	@Override
	public void createEvent(CreateEventRequest request, StreamObserver<Result> responseObserver) {

		EventMessage eventMessage = new EventMessage(request.getEvent());

		ServiceUtil.saveEntity(eventMessage, eventRepository);

		Result result = ServiceUtil.returnSuccessful("Event creation successful.");

		ServiceUtil.configureResponseObserver(responseObserver, result);
	}

	@Override
	public void readEvent(UserRequest request, StreamObserver<ReadEventResult> responseObserver) {

		List<EventEntity> allEventEntities = eventRepository.findAll();

		List<Event> allEvents = allEventEntities.stream()
				.map(eventEntity -> eventEntity.parseEntity().getEvent())
				.collect(Collectors.toList());

		ReadEventResult readEventResult = ReadEventResult.newBuilder()
				.addAllEvents(allEvents).build();

		ServiceUtil.configureResponseObserver(responseObserver, readEventResult);
	}

	@Override
	public void readEventById(ReadByIdRequest request, StreamObserver<ReadEventByIdResult> responseObserver) {

		EventEntity eventEntity;

		try {

			eventEntity = eventRepository
					.findById(request.getReadId())
					.orElseThrow(IllegalArgumentException::new);

		} catch (IllegalArgumentException illegalArgumentException) {

			ReadEventByIdResult result = ReadEventByIdResult
					.newBuilder()
					.setEvent((Event) null)
					.build();

			ServiceUtil.configureResponseObserver(responseObserver, result);

			return;
		}

		Event event = eventEntity.parseEntity().getEvent();

		ReadEventByIdResult readEventByIdResult = ReadEventByIdResult
				.newBuilder()
				.setEvent(event)
				.build();

		ServiceUtil.configureResponseObserver(responseObserver, readEventByIdResult);

	}

	@Override
	public void updateEventInfo(UpdateEventInfoRequest request, StreamObserver<Result> responseObserver) {

		long eventId = request.getEvent().getId();

		boolean deleteSuccessful = ServiceUtil.deleteEntity(eventId, eventRepository);

		if (!deleteSuccessful) {

			Result result = ServiceUtil.returnError("Cannot find event from given ID.");

			ServiceUtil.configureResponseObserver(responseObserver, result);

			return;

		}

		EventMessage eventMessage = new EventMessage(request.getEvent());

		ServiceUtil.saveEntity(eventMessage, eventRepository);

		Result result = ServiceUtil.returnSuccessful("Event update successful.");

		ServiceUtil.configureResponseObserver(responseObserver, result);
	}

	@Override
	public void deleteEvent(DeleteEventRequest request, StreamObserver<Result> responseObserver) {

		long eventId = request.getEventId();

		boolean deleteSuccessful = ServiceUtil.deleteEntity(eventId, eventRepository);

		if (!deleteSuccessful) {

			Result result = ServiceUtil.returnError("Cannot find event from given ID.");

			ServiceUtil.configureResponseObserver(responseObserver, result);

			return;

		}

		Result result = ServiceUtil.returnSuccessful("Event deletion successful.");

		ServiceUtil.configureResponseObserver(responseObserver, result);
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
	public void updateRegistrationRequest(UpdateRegistrationRequestRequest request, StreamObserver<Result> responseObserver) {
		super.updateRegistrationRequest(request, responseObserver);
	}

	@Override
	public void hasEvent(HasEventRequest request, StreamObserver<Result> responseObserver) {

	}
}
