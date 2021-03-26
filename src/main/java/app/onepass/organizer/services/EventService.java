package app.onepass.organizer.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.protobuf.Empty;

import app.onepass.apis.CreateEventRequest;
import app.onepass.apis.Duration;
import app.onepass.apis.Event;
import app.onepass.apis.HasEventRequest;
import app.onepass.apis.OrganizerServiceGrpc;
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
	private EventRepository eventRepository;

	@Autowired
	private EventDurationRepository eventDurationRepository;

	@Autowired
	private UserEventRepository userEventRepository;

	@Override
	@Transactional
	public void createEvent(CreateEventRequest request, StreamObserver<Empty> responseObserver) {

		if (eventRepository.findById(request.getEvent().getId()).isPresent()) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "An event with this ID already exists.");

			return;
		}

		EventMessage eventMessage = new EventMessage(request.getEvent());

		ServiceUtil.saveEntity(eventMessage, eventRepository);

		responseObserver.onCompleted();
	}



	@Override
	@Transactional
	public void updateEvent(UpdateEventRequest request, StreamObserver<Empty> responseObserver) {

		if (!eventRepository.findById(request.getEvent().getId()).isPresent()) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "An event with this ID does not exist.");

			return;
		}

		EventMessage eventMessage = new EventMessage(request.getEvent());

		ServiceUtil.saveEntity(eventMessage, eventRepository);

		responseObserver.onCompleted();
	}

	@Override
	@Transactional
	public void removeEvent(RemoveEventRequest request, StreamObserver<Empty> responseObserver) {

		long eventId = request.getEventId();

		boolean deleteSuccessful = ServiceUtil.deleteEntity(eventId, eventRepository);

		if (!deleteSuccessful) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "Cannot find event from given ID.");

			return;
		}

		responseObserver.onCompleted();
	}

	@Override
	@Transactional
	public void updateEventDurations(UpdateEventDurationRequest request, StreamObserver<Empty> responseObserver) {

		long eventId = request.getEventId();

		eventDurationRepository.deleteByEventId(eventId);

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

		responseObserver.onCompleted();
	}

	@Override
	@Transactional
	public void updateRegistrationRequest(UpdateRegistrationRequestRequest request, StreamObserver<Empty> responseObserver) {

		UserEventEntity userEventEntity = userEventRepository.findByUserIdAndEventId(request.getRegisteredUserId(), request.getRegisteredEventId());

		userEventEntity.setStatus(request.getStatus().toString());

		userEventRepository.save(userEventEntity);

		responseObserver.onCompleted();
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
