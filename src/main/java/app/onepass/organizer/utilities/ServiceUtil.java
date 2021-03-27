package app.onepass.organizer.utilities;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.protobuf.Empty;

import app.onepass.apis.HasPermissionRequest;
import app.onepass.apis.Permission;
import app.onepass.organizer.entities.BaseEntity;
import app.onepass.organizer.entities.EventEntity;
import app.onepass.organizer.messages.BaseMessage;
import app.onepass.organizer.repositories.EventRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class ServiceUtil {

	public static <M extends BaseMessage<M, E>, E extends BaseEntity<M, E>> boolean saveEntity(BaseMessage<M, E> message,
			JpaRepository<E, Long> repository) {

		E entity = message.parseMessage();

		repository.save(entity);

		return true;
	}

	public static <M extends BaseMessage<M, E>, E extends BaseEntity<M, E>> boolean deleteEntity(long id,
			JpaRepository<E, Long> repository) {

		E entity;

		try {

			entity = repository.findById(id).orElseThrow(IllegalArgumentException::new);

		} catch (IllegalArgumentException illegalArgumentException) {

			return false;
		}

		repository.delete(entity);

		return true;
	}

	public static <T> void returnInvalidArgumentError(StreamObserver<T> responseObserver, String description) {

		responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(description).asException());
	}

	public static <T> void returnPermissionDeniedError(StreamObserver<T> responseObserver) {

		responseObserver.onError(Status.PERMISSION_DENIED.withDescription("The user has no permission to execute the specified operation.").asException());
	}

	public static <T> void returnObject(StreamObserver<T> responseObserver, T object) {

		responseObserver.onNext(object);

		responseObserver.onCompleted();
	}

	public static void returnEmpty(StreamObserver<Empty> responseObserver) {

		responseObserver.onNext(Empty.newBuilder().build());

        responseObserver.onCompleted();
	}

	public static HasPermissionRequest createHasPermissionRequest(long userId, long organizationId, Permission permission) {

		return HasPermissionRequest.newBuilder()
				.setUserId(userId)
				.setOrganizationId(organizationId)
				.setPermissionName(permission)
				.build();
	}

	public static <T> long getOrganizationIdFromEventId(EventRepository eventRepository, StreamObserver<T> responseObserver, long eventId) {

		EventEntity eventEntity;

		eventEntity = eventRepository.findById(eventId).orElseThrow(IllegalArgumentException::new);

		return eventEntity.getOrganizationId();
	}
}
