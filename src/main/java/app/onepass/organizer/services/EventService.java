package app.onepass.organizer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.onepass.apis.CreateEventRequest;
import app.onepass.apis.Duration;
import app.onepass.apis.Event;
import app.onepass.apis.GetByIdRequest;
import app.onepass.apis.GetEventByIdResponse;
import app.onepass.apis.GetEventResponse;
import app.onepass.apis.HasEventRequest;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.RemoveEventRequest;
import app.onepass.apis.Result;
import app.onepass.apis.UpdateEventDurationRequest;
import app.onepass.apis.UpdateEventFacilityRequest;
import app.onepass.apis.UpdateEventInfoRequest;
import app.onepass.apis.UpdateRegistrationRequestRequest;
import app.onepass.apis.UserRequest;
import app.onepass.organizer.entities.EventDurationEntity;
import app.onepass.organizer.entities.EventEntity;
import app.onepass.organizer.entities.EventRegistrationEntity;
import app.onepass.organizer.messages.EventMessage;
import app.onepass.organizer.messages.FacilityMessage;
import app.onepass.organizer.repositories.EventDurationRepository;
import app.onepass.organizer.repositories.EventRegistrationRepository;
import app.onepass.organizer.repositories.EventRepository;
import app.onepass.organizer.repositories.FacilityRepository;
import app.onepass.organizer.utilities.ServiceUtil;
import io.grpc.stub.StreamObserver;

@Service
public class EventService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private FacilityRepository facilityRepository;

	@Autowired
	private EventDurationRepository eventDurationRepository;

	@Autowired
	private EventRegistrationRepository eventRegistrationRepository;

	@Override
	public void createEvent(CreateEventRequest request, StreamObserver<Result> responseObserver) {

		EventMessage eventMessage = new EventMessage(request.getEvent());

		ServiceUtil.saveEntity(eventMessage, eventRepository);

		Result result = ServiceUtil.returnSuccessful("Event creation successful.");

		ServiceUtil.configureResponseObserver(responseObserver, result);
	}

	@Override
	public void getEvent(UserRequest request, StreamObserver<GetEventResponse> responseObserver) {

		List<EventEntity> allEventEntities = eventRepository.findAll();

		List<Event> allEvents = allEventEntities.stream()
				.map(eventEntity -> eventEntity.parseEntity().getEvent())
				.collect(Collectors.toList());

		GetEventResponse readEventResponse = GetEventResponse.newBuilder()
				.addAllEvents(allEvents).build();

		ServiceUtil.configureResponseObserver(responseObserver, readEventResponse);
	}

	@Override
	public void getEventById(GetByIdRequest request, StreamObserver<GetEventByIdResponse> responseObserver) {

		EventEntity eventEntity;

		try {

			eventEntity = eventRepository
					.findById(request.getReadId())
					.orElseThrow(IllegalArgumentException::new);

		} catch (IllegalArgumentException illegalArgumentException) {

			GetEventByIdResponse result = GetEventByIdResponse
					.newBuilder()
					.setEvent((Event) null)
					.build();

			ServiceUtil.configureResponseObserver(responseObserver, result);

			return;
		}

		Event event = eventEntity.parseEntity().getEvent();

		GetEventByIdResponse readEventByIdResponse = GetEventByIdResponse
				.newBuilder()
				.setEvent(event)
				.build();

		ServiceUtil.configureResponseObserver(responseObserver, readEventByIdResponse);

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
	public void removeEvent(RemoveEventRequest request, StreamObserver<Result> responseObserver) {

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

		long facilityId = request.getFacility().getId();

		boolean deleteSuccessful = ServiceUtil.deleteEntity(facilityId, facilityRepository);

		if (!deleteSuccessful) {

			Result result = ServiceUtil.returnError("Cannot find facility from given ID.");

			ServiceUtil.configureResponseObserver(responseObserver, result);

			return;

		}

		FacilityMessage facilityMessage = new FacilityMessage(request.getFacility());

		ServiceUtil.saveEntity(facilityMessage, facilityRepository);

		Result result = ServiceUtil.returnSuccessful("Facility update successful.");

		ServiceUtil.configureResponseObserver(responseObserver, result);
	}

	@Override
	public void updateEventDuration(UpdateEventDurationRequest request, StreamObserver<Result> responseObserver) {

		long eventId = request.getEventId();

		eventDurationRepository.deleteByEventId(eventId);

		List<EventDurationEntity> entitiesToAdd = new ArrayList<>();

		List<Duration> durations = request.getDurationList();

		for (Duration duration : durations) {

			EventDurationEntity eventDurationEntity = EventDurationEntity.builder()
					.eventId(eventId)
					.start(duration.getStart())
					.finish(duration.getFinish())
					.build();

			entitiesToAdd.add(eventDurationEntity);
		}

		eventDurationRepository.saveAll(entitiesToAdd);

		Result result = ServiceUtil.returnSuccessful("Event duration update successful.");

		ServiceUtil.configureResponseObserver(responseObserver, result);

	}

	@Override
	public void updateRegistrationRequest(UpdateRegistrationRequestRequest request, StreamObserver<Result> responseObserver) {

		eventRegistrationRepository.deleteByEventIdAndUserId(request.getRegisteredEventId(), request.getRegisteredUserId());

		EventRegistrationEntity eventRegistrationEntity = EventRegistrationEntity.builder()
				.eventId(request.getRegisteredEventId())
				.userId(request.getRegisteredUserId())
				.status(request.getStatus())
				.build();

		eventRegistrationRepository.save(eventRegistrationEntity);

		Result result = ServiceUtil.returnSuccessful("Event registration update successful.");

		ServiceUtil.configureResponseObserver(responseObserver, result);
	}

	@Override
	public void hasEvent(HasEventRequest request, StreamObserver<Result> responseObserver) {

		EventEntity eventEntity;

		try {

			eventEntity = eventRepository
					.findById(request.getEventId())
					.orElseThrow(IllegalArgumentException::new);

		} catch (IllegalArgumentException illegalArgumentException) {

			Result result = ServiceUtil.returnError("Cannot find organization from given ID.");

			ServiceUtil.configureResponseObserver(responseObserver, result);

			return;
		}

		long organizationId = eventEntity.getOrganizationId();

		if (organizationId == request.getOrganizationId()) {
			ServiceUtil.returnSuccessful("The specified organization has the specified event.");
		} else {
			ServiceUtil.returnError("The event is not hosted by this organization");
		}
	}
}
