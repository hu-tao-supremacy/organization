package app.onepass.organizer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.onepass.apis.CheckInRequest;
import app.onepass.apis.CreateEventRequest;
import app.onepass.apis.Duration;
import app.onepass.apis.Event;
import app.onepass.apis.EventDuration;
import app.onepass.apis.EventDurationListResponse;
import app.onepass.apis.GenerateTicketRequest;
import app.onepass.apis.HasEventRequest;
import app.onepass.apis.HasPermissionRequest;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.Permission;
import app.onepass.apis.RemoveEventRequest;
import app.onepass.apis.UpdateEventDurationsRequest;
import app.onepass.apis.UpdateEventRequest;
import app.onepass.apis.UpdateRegistrationRequestRequest;
import app.onepass.apis.UserEvent;
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
	public void createEvent(CreateEventRequest request, StreamObserver<Event> responseObserver) {

		HasPermissionRequest hasPermissionRequest = ServiceUtil.createHasPermissionRequest(request.getUserId(),
				request.getEvent().getOrganizationId(), Permission.EVENT_CREATE);

		if (!accountService.hasPermission(hasPermissionRequest).getValue()) {

			ServiceUtil.returnPermissionDeniedError(responseObserver);

			return;
		}

		if (eventRepository.existsById(request.getEvent().getId())) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "An event with this ID already exists.");

			return;
		}

		EventMessage eventMessage = new EventMessage(request.getEvent());

		EventEntity savedEntity = eventRepository.save(eventMessage.parseMessage());

		ServiceUtil.returnObject(responseObserver, savedEntity.parseEntity().getEvent());
	}

	@Override
	public void updateEvent(UpdateEventRequest request, StreamObserver<Event> responseObserver) {

		int storedOrganizationId = ServiceUtil.getOrganizationIdFromEventId(eventRepository, request.getEvent().getId());

		int requestedOrganizationId = request.getEvent().getOrganizationId();

		if (storedOrganizationId != requestedOrganizationId) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "Cannot change organization ID in an event update.");

			return;
		}

		if (!ServiceUtil.hasValidParameters(accountService, eventRepository, responseObserver, request.getUserId(),
				request.getEvent().getId(), Permission.EVENT_UPDATE)) {

			return;
		}

		if (!eventRepository.existsById(request.getEvent().getId())) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "An event with this ID does not exist.");

			return;
		}

		EventMessage eventMessage = new EventMessage(request.getEvent());

		EventEntity savedEntity = eventRepository.save(eventMessage.parseMessage());

		ServiceUtil.returnObject(responseObserver, savedEntity.parseEntity().getEvent());
	}

	@Override
	public void removeEvent(RemoveEventRequest request, StreamObserver<Event> responseObserver) {

		if (!ServiceUtil.hasValidParameters(accountService, eventRepository, responseObserver, request.getUserId(),
				request.getEventId(), Permission.EVENT_REMOVE)) {

			return;
		}

		int eventId = request.getEventId();

		EventEntity eventEntity;

		try {

			eventEntity = eventRepository.findById(eventId).orElseThrow(IllegalArgumentException::new);

		} catch (IllegalArgumentException illegalArgumentException) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "Cannot find event from given ID.");

			return;
		}

		eventRepository.delete(eventEntity);

		ServiceUtil.returnObject(responseObserver, eventEntity.parseEntity().getEvent());
	}

	@Override
	public void updateEventDurations(UpdateEventDurationsRequest request, StreamObserver<EventDurationListResponse> responseObserver) {

		if (!ServiceUtil.hasValidParameters(accountService, eventRepository, responseObserver, request.getUserId(),
				request.getEventId(), Permission.EVENT_UPDATE)) {

			return;
		}

		int eventId = request.getEventId();

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

		List<EventDurationEntity> addedEntities = eventDurationRepository.saveAll(entitiesToAdd);

		List<EventDuration> eventDurations = addedEntities.stream()
				.map(eventDurationEntity -> eventDurationEntity.parseEntity().getEventDuration())
				.collect(Collectors.toList());

		EventDurationListResponse eventDurationListResponse = EventDurationListResponse.newBuilder()
				.addAllEventDurations(eventDurations)
				.build();

		ServiceUtil.returnObject(responseObserver, eventDurationListResponse);
	}

	@Override
	public void updateRegistrationRequest(UpdateRegistrationRequestRequest request, StreamObserver<UserEvent> responseObserver) {

		if (!ServiceUtil.hasValidParameters(accountService, eventRepository, responseObserver, request.getUserId(),
				request.getRegisteredEventId(), Permission.EVENT_UPDATE)) {

			return;
		}

		UserEventEntity userEventEntity = userEventRepository.findByUserIdAndEventId(request.getRegisteredUserId(),
				request.getRegisteredEventId());

		if (userEventEntity == null) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "The user has not been registered in this event.");

			return;
		}

		userEventEntity.setStatus(request.getStatus().toString());

		if (request.getStatus().equals(UserEvent.Status.APPROVED) && userEventEntity.getTicket() == null) {

			userEventEntity.setTicket(createTicket());

		}

		UserEventEntity savedEntity = userEventRepository.save(userEventEntity);

		ServiceUtil.returnObject(responseObserver, savedEntity.parseEntity().getUserEvent());
	}

	@Override
	public void hasEvent(HasEventRequest request, StreamObserver<Event> responseObserver) {

		EventEntity eventEntity;

		try {

			eventEntity = eventRepository.findById(request.getEventId()).orElseThrow(IllegalArgumentException::new);

		} catch (IllegalArgumentException illegalArgumentException) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "The event does not exist.");

			return;
		}

		int organizationId = eventEntity.getOrganizationId();

		if (organizationId == request.getOrganizationId()) {

			ServiceUtil.returnObject(responseObserver, eventEntity.parseEntity().getEvent());

		} else {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "The event is not hosted by this organization.");

		}
	}

	@Override
	public void generateTicket(GenerateTicketRequest request, StreamObserver<UserEvent> responseObserver) {

		UserEventEntity userEventEntity = userEventRepository.findByUserIdAndEventId(request.getUserId(), request.getEventId());

		userEventEntity.setTicket(createTicket());

		UserEventEntity savedEntity = userEventRepository.save(userEventEntity);

		ServiceUtil.returnObject(responseObserver, savedEntity.parseEntity().getUserEvent());
	}

	@Override
	public void checkIn(CheckInRequest request, StreamObserver<UserEvent> responseObserver) {

		UserEventEntity userEventEntity = userEventRepository.findByTicketAndEventId(request.getTicket(), request.getEventId());

		userEventEntity.setStatus("ATTENDED");

		UserEventEntity savedEntity = userEventRepository.save(userEventEntity);

		ServiceUtil.returnObject(responseObserver, savedEntity.parseEntity().getUserEvent());
	}

	private String createTicket() {

		String ticketCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

		int ticketLength = 6;

		return (new Random()).ints(ticketLength, 0, ticketCharacters.length() - 1)
				.map(ticketCharacters::charAt)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();

	}
}
