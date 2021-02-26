package app.onepass.organizer.utilities;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.protobuf.GeneratedMessageV3;
import com.sun.xml.bind.v2.model.core.ID;

import app.onepass.apis.Result;
import app.onepass.organizer.entities.BaseEntity;
import app.onepass.organizer.entities.OrganizationEntity;
import app.onepass.organizer.messages.BaseMessage;
import io.grpc.stub.StreamObserver;

public class ServiceUtil {

	    private <M extends BaseMessage<M, E>, E extends BaseEntity<M, E>> void
		saveEntity(BaseMessage<M, E> message, JpaRepository<E, Long> repository) {

			E entity = message.parseMessage();

			repository.save(entity);
	    }

	    private <M extends BaseMessage<M, E>, E extends BaseEntity<M, E>, R extends GeneratedMessageV3> boolean
		deleteEntity(String entityName, E entity, long id, JpaRepository<E, Long> repository, StreamObserver<R> responseObserver) {

			try {
				organizationEntity = repository
						.findById(id)
						.orElseThrow(IllegalArgumentException::new);
			} catch (IllegalArgumentException illegalArgumentException) {
				Result result = returnError("Cannot find ".concat(entityName).concat(" from given ID."));
				configureResponseObserver(responseObserver, result);
				return;
			}

			organizationRepository.delete(organizationEntity);
	        }
	    }

	public static Result returnError(String description) {
		return Result.newBuilder().setIsOk(false).setDescription(description).build();
	}

	public static Result returnSuccessful(String description) {
		return Result.newBuilder().setIsOk(true).setDescription(description).build();
	}

	public static <T> void configureResponseObserver(StreamObserver<T> responseObserver, T result) {
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}
}
