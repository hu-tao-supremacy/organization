package app.onepass.organizer.utilities;

import org.springframework.data.jpa.repository.JpaRepository;

import app.onepass.apis.Result;
import app.onepass.organizer.entities.BaseEntity;
import app.onepass.organizer.messages.BaseMessage;
import io.grpc.stub.StreamObserver;

public class ServiceUtil {

	    public static <M extends BaseMessage<M, E>, E extends BaseEntity<M, E>> boolean
		saveEntity(BaseMessage<M, E> message, JpaRepository<E, Long> repository) {

			E entity = message.parseMessage();

			repository.save(entity);

			return true;
	    }

	    public static <M extends BaseMessage<M, E>, E extends BaseEntity<M, E>> boolean
		deleteEntity(long id, JpaRepository<E, Long> repository) {

	    	E entity;

			try {

				entity = repository.findById(id).orElseThrow(IllegalArgumentException::new);

			} catch (IllegalArgumentException illegalArgumentException) {

				return false;
			}

			repository.delete(entity);

			return true;
	    }

	public static Result returnError(String description) {
		return Result.newBuilder().setIsOk(false).setDescription(description).build();
	}

	public static Result returnSuccessful(String description) {
		return Result.newBuilder().setIsOk(true).setDescription(description).build();
	}

	public static <R> void configureResponseObserver(StreamObserver<R> responseObserver, R result) {
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}
}
