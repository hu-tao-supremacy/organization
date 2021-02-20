package app.onepass.organizer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import app.onepass.apis.CreateEventReq;
import app.onepass.apis.Organization;
import app.onepass.apis.OrganizationServiceGrpc;
import app.onepass.apis.ReadOrganizationRes;
import app.onepass.apis.Result;
import app.onepass.apis.UserReq;
import app.onepass.organizer.entities.OrganizationEntity;
import app.onepass.organizer.services.OrganizationService;
import app.onepass.organizer.utilities.EntityParser;
import io.grpc.stub.StreamObserver;

public class OrganizerImpl extends OrganizationServiceGrpc.OrganizationServiceImplBase {

	@Autowired
	public OrganizationService organizationService;

	@Override
	public void createEvent(CreateEventReq request, StreamObserver<Result> responseObserver) {
		responseObserver.onNext(
				Result.newBuilder().setIsOk(true).setDescription("Jean").build()
							   );
		responseObserver.onCompleted();
	}

	@Override
	public void readOrganization(UserReq request, StreamObserver<ReadOrganizationRes> responseObserver) {

		List<OrganizationEntity> allOrganizationEntities = organizationService.getAllOrganizations();

		List<Organization> allOrganizations = allOrganizationEntities.stream()
				.map(EntityParser::parseOrganization)
				.collect(Collectors.toList());

		responseObserver.onNext(
				ReadOrganizationRes.newBuilder().addAllOrganizations(allOrganizations).build());
		
		responseObserver.onCompleted();
	}
}
