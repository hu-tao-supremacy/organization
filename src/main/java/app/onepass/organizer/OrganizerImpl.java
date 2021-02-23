package app.onepass.organizer;

import java.util.List;
import java.util.stream.Collectors;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import app.onepass.apis.CreateOrganizationReq;
import app.onepass.apis.DeleteOrganizationReq;
import app.onepass.apis.Organization;
import app.onepass.apis.OrganizationServiceGrpc;
import app.onepass.apis.ReadOrganizationRes;
import app.onepass.apis.Result;
import app.onepass.apis.UpdateOrganizationReq;
import app.onepass.apis.UserReq;
import app.onepass.organizer.entities.OrganizationEntity;
import app.onepass.organizer.repositories.OrganizationRepository;
import app.onepass.organizer.services.OrganizationService;
import app.onepass.organizer.utilities.EntityParser;
import io.grpc.stub.StreamObserver;

@GRpcService
public class OrganizerImpl extends OrganizationServiceGrpc.OrganizationServiceImplBase {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public void createOrganization(CreateOrganizationReq request, StreamObserver<Result> responseObserver) {

        Organization organization = request.getOrganization();

        OrganizationEntity organizationEntity = EntityParser.parseOrganization(organization);

        organizationRepository.save(organizationEntity);
    }

    @Override
    public void readOrganization(UserReq request, StreamObserver<ReadOrganizationRes> responseObserver) {

        List<OrganizationEntity> allOrganizationEntities = organizationService.getAllOrganizations();

        List<Organization> allOrganizations = allOrganizationEntities.stream()
                .map(EntityParser::parseOrganizationEntity)
                .collect(Collectors.toList());

        ReadOrganizationRes readOrganizationRes = ReadOrganizationRes.newBuilder()
                .addAllOrganizations(allOrganizations).build();

        configureResponseObserver(responseObserver, readOrganizationRes);

    }

    @Override
    public void updateOrganization(UpdateOrganizationReq request, StreamObserver<Result> responseObserver) {

        long organizationId = request.getOrganizationId();

        try (OrganizationEntity organizationEntity = new OrganizationEntity()) {
            OrganizationEntity organizationEntity = organizationRepository
                    .findById(organizationId)
                    .orElseThrow(IllegalArgumentException::new);
        } catch (IllegalArgumentException illegalArgumentException) {
            returnError("Cannot find organization from given ID.");
        }
        request.getOrganization();
    }

    @Override
    public void deleteOrganization(DeleteOrganizationReq request, StreamObserver<Result> responseObserver) {
        super.deleteOrganization(request, responseObserver);
    }

    private Result returnError(String description) {
        return Result.newBuilder().setIsOk(false).setDescription(description).build();
    }

    private <T> void configureResponseObserver(StreamObserver<T> responseObserver, T result) {
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }
}