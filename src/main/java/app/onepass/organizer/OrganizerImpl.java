package app.onepass.organizer;

import java.util.List;
import java.util.stream.Collectors;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import app.onepass.apis.Organization;
import app.onepass.apis.OrganizationServiceGrpc;
import app.onepass.apis.ReadOrganizationRes;
import app.onepass.apis.UserReq;
import app.onepass.organizer.entities.OrganizationEntity;
import app.onepass.organizer.services.OrganizationService;
import app.onepass.organizer.utilities.EntityParser;
import io.grpc.stub.StreamObserver;

@GRpcService
public class OrganizerImpl extends OrganizationServiceGrpc.OrganizationServiceImplBase {

    @Autowired
    private OrganizationService organizationService;

    @Override
    public void readOrganization(UserReq request, StreamObserver<ReadOrganizationRes> responseObserver) {

        List<OrganizationEntity> allOrganizationEntities = organizationService.getAllOrganizations();

        List<Organization> allOrganizations = allOrganizationEntities.stream()
                .map(EntityParser::parseOrganization)
                .collect(Collectors.toList());

        ReadOrganizationRes readOrganizationRes = ReadOrganizationRes.newBuilder()
                .addAllOrganizations(allOrganizations).build();

        configureResponseObserver(responseObserver, readOrganizationRes);

    }

    private <T> void configureResponseObserver(StreamObserver<T> responseObserver, T result) {
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }
}