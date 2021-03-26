package app.onepass.organizer.utilities;

import org.springframework.data.jpa.repository.JpaRepository;

import app.onepass.organizer.entities.BaseEntity;
import app.onepass.organizer.messages.BaseMessage;
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

		responseObserver.onCompleted();
	}

	public static <T> void returnObject(StreamObserver<T> responseObserver, T object) {

		responseObserver.onNext(object);

		responseObserver.onCompleted();
	}
}
