package app.onepass.organizer.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.Empty;

import app.onepass.apis.CreateEventRequest;
import app.onepass.apis.Duration;
import app.onepass.apis.Event;
import app.onepass.apis.HasEventRequest;
import app.onepass.apis.HasPermissionRequest;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.Permission;
import app.onepass.apis.RemoveEventRequest;
import app.onepass.apis.UpdateEventDurationRequest;
import app.onepass.apis.UpdateEventRequest;
import app.onepass.apis.UpdateRegistrationRequestRequest;
import app.onepass.organizer.entities.EventDurationEntity;
import app.onepass.organizer.entities.EventEntity;
import app.onepass.organizer.entities.UserEventEntity;
import app.onepass.organizer.messages.EventMessage;
import app.onepass.organizer.repositories.EventDurationRepository;
import app.onepass.organizer.repositories.EventRepository;
import app.onepass.organizer.repositories.UserEventRepository;
import app.onepass.organizer.utilities.ServiceUtil;
import app.onepass.organizer.utilities.TypeUtil;
import io.grpc.stub.StreamObserver;

@Service
public class EventService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Autowired
	private AccountService accountService;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventDurationRepository eventDurationRepository;

	@Autowired
	private UserEventRepository userEventRepository;

	@Override
	public void createEvent(CreateEventRequest request, StreamObserver<Empty> responseObserver) {

		HasPermissionRequest hasPermissionRequest = ServiceUtil.createHasPermissionRequest(request.getUserId(), request.getEvent().getOrganizationId(), Permission.EVENT_CREATE);

		if (!accountService.hasPermission(hasPermissionRequest).getValue()) {

			ServiceUtil.returnPermissionDeniedError(responseObserver);

			return;
		}

		if (eventRepository.findById(request.getEvent().getId()).isPresent()) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "An event with this ID already exists.");

			return;
		}

		EventMessage eventMessage = new EventMessage(request.getEvent());

		ServiceUtil.saveEntity(eventMessage, eventRepository);

		ServiceUtil.returnEmpty(responseObserver);
	}

	@Override
	public void updateEvent(UpdateEventRequest request, StreamObserver<Empty> responseObserver) {

		long storedOrganizationId = ServiceUtil.getOrganizationIdFromEventId(eventRepository, responseObserver, request.getEvent().getId()));

		long requestedOrganizationId = request.getEvent().getOrganizationId();

		if (storedOrganizationId != requestedOrganizationId) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "Cannot change organization ID in an event update.");

			return;
		}

		if (!ServiceUtil.hasValidParameters(
				accountService,
				eventRepository,
				responseObserver,
				request.getUserId(),
				request.getEvent().getId(),
				Permission.EVENT_UPDATE)) {

			return;
		}

		if (!eventRepository.findById(request.getEvent().getId()).isPresent()) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "An event with this ID does not exist.");

			return;
		}

		EventMessage eventMessage = new EventMessage(request.getEvent());

		ServiceUtil.saveEntity(eventMessage, eventRepository);

		ServiceUtil.returnEmpty(responseObserver);
	}

	@Override
	public void removeEvent(RemoveEventRequest request, StreamObserver<Empty> responseObserver) {

		if (!ServiceUtil.hasValidParameters(
				accountService,
				eventRepository,
				responseObserver,
				request.getUserId(),
				request.getEventId(),
				Permission.EVENT_REMOVE)) {

			return;
		}

		long eventId = request.getEventId();

		boolean deleteSuccessful = ServiceUtil.deleteEntity(eventId, eventRepository);

		if (!deleteSuccessful) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "Cannot find event from given ID.");

			return;
		}

		ServiceUtil.returnEmpty(responseObserver);
	}

	@Override
	public void updateEventDurations(UpdateEventDurationRequest request, StreamObserver<Empty> responseObserver) {

		if (!ServiceUtil.hasValidParameters(
				accountService,
				eventRepository,
				responseObserver,
				request.getUserId(),
				request.getEventId(),
				Permission.EVENT_UPDATE)) {

			return;
		}

		long eventId = request.getEventId();

		eventDurationRepository.deleteAllByEventId(eventId);

		List<EventDurationEntity> entitiesToAdd = new ArrayList<>();

		List<Duration> durations = request.getDurationList();

		for (Duration duration : durations) {

			EventDurationEntity eventDurationEntity = EventDurationEntity.builder()
					.eventId(eventId)
					.start(TypeUtil.toSqlTimestamp(duration.getStart()))
					.finish(TypeUtil.toSqlTimestamp(duration.getFinish()))
					.build();

			entitiesToAdd.add(eventDurationEntity);
		}

		eventDurationRepository.saveAll(entitiesToAdd);

		ServiceUtil.returnEmpty(responseObserver);
	}

	@Override
	public void updateRegistrationRequest(UpdateRegistrationRequestRequest request, StreamObserver<Empty> responseObserver) {

		if (!ServiceUtil.hasValidParameters(
				accountService,
				eventRepository,
				responseObserver,
				request.getUserId(),
				request.getRegisteredEventId(),
				Permission.EVENT_UPDATE)) {

			return;
		}

		UserEventEntity userEventEntity = userEventRepository.findByUserIdAndEventId(request.getRegisteredUserId(), request.getRegisteredEventId());

		userEventEntity.setStatus(request.getStatus().toString());

		userEventRepository.save(userEventEntity);

		ServiceUtil.returnEmpty(responseObserver);
	}

	@Override
	public void hasEvent(HasEventRequest request, StreamObserver<Event> responseObserver) {

		EventEntity eventEntity;

		try {

			eventEntity = eventRepository
					.findById(request.getEventId())
					.orElseThrow(IllegalArgumentException::new);

		} catch (IllegalArgumentException illegalArgumentException) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "The event does not exist.");

			return;
		}

		long organizationId = eventEntity.getOrganizationId();

		if (organizationId == request.getOrganizationId()) {

			ServiceUtil.returnObject(responseObserver, eventEntity.parseEntity().getEvent());

		} else {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "The event is not hosted by this organization.");

		}
	}
}
