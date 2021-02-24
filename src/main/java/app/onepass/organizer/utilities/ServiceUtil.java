package app.onepass.organizer.utilities;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.protobuf.MessageOrBuilder;

import app.onepass.apis.Organization;
import app.onepass.apis.Result;
import app.onepass.organizer.entities.BaseEntity;
import app.onepass.organizer.entities.OrganizationEntity;
import io.grpc.stub.StreamObserver;

public class ServiceUtil {


	    private void saveEntity(MessageOrBuilder message, JpaRepository repository) {

			BaseEntity entity = message.parseIntoEntity();

			repository.save(entity);
	    }
//
//	    private boolean deleteEntity(String entityName, Object entity, long id) {
//
//	        if (entityName.equals("organization")) {
//
//	            long organizationId = id;
//
//	            OrganizationEntity organizationEntity;
//
//	            try {
//	                organizationEntity = organizationRepository
//	                        .findById(organizationId)
//	                        .orElseThrow(IllegalArgumentException::new);
//	            } catch (IllegalArgumentException illegalArgumentException) {
//	                Result result = returnError("Cannot find organization from given ID.");
//	                configureResponseObserver(responseObserver, result);
//	                return;
//	            }
//
//	            organizationRepository.delete(organizationEntity);
//	        }
//	    }

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
