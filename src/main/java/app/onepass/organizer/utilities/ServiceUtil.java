package app.onepass.organizer.utilities;

import app.onepass.apis.Organization;
import app.onepass.apis.Result;
import app.onepass.organizer.entities.OrganizationEntity;
import io.grpc.stub.StreamObserver;

public class ServiceUtil {


	    private void saveEntity(String entityName, Object entity) {

	        if (entityName.equals("organization")) {

	            Organization organization = (Organization) entity;

	            OrganizationEntity organizationEntity = EntityParser.parseOrganization(organization)

	            organizationRepository.save(organizationEntity);
	        }
	    }

	    private boolean deleteEntity(String entityName, Object entity, long id) {

	        if (entityName.equals("organization")) {

	            long organizationId = id;

	            OrganizationEntity organizationEntity;

	            try {
	                organizationEntity = organizationRepository
	                        .findById(organizationId)
	                        .orElseThrow(IllegalArgumentException::new);
	            } catch (IllegalArgumentException illegalArgumentException) {
	                Result result = returnError("Cannot find organization from given ID.");
	                configureResponseObserver(responseObserver, result);
	                return;
	            }

	            organizationRepository.delete(organizationEntity);
	        }
	    }

	private Result returnError(String description) {
		return Result.newBuilder().setIsOk(false).setDescription(description).build();
	}

	private Result returnSuccessful(String description) {
		return Result.newBuilder().setIsOk(true).setDescription(description).build();
	}

	private <T> void configureResponseObserver(StreamObserver<T> responseObserver, T result) {
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}
}
