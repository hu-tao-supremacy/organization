package app.onepass.organizer.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import static app.onepass.organizer.utilities.EntityParser.parseOrganization;

import app.onepass.apis.CreateOrganizationRequest;
import app.onepass.apis.DeleteOrganizationRequest;
import app.onepass.apis.Organization;
import app.onepass.apis.OrganizationServiceGrpc;
import app.onepass.apis.ReadOrganizationRes;
import app.onepass.apis.ReadOrganizationResult;
import app.onepass.apis.Result;
import app.onepass.apis.UpdateOrganizationRequest;
import app.onepass.apis.UserRequest;
import app.onepass.organizer.entities.OrganizationEntity;
import app.onepass.organizer.messages.OrganizationMessage;
import app.onepass.organizer.repositories.OrganizationRepository;
import app.onepass.organizer.services.OrganizationService;
import app.onepass.organizer.utilities.ServiceUtil;
import io.grpc.stub.StreamObserver;

@GRpcService
public class OrganizationImpl extends OrganizationServiceGrpc.OrganizationServiceImplBase {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public void createOrganization(CreateOrganizationRequest request, StreamObserver<Result> responseObserver) {

        OrganizationMessage organizationMessage = new OrganizationMessage(request.getOrganization());

        OrganizationEntity organizationEntity = organizationMessage.parseMessage();

        organizationRepository.save(organizationEntity);

        Result result = ServiceUtil.returnSuccessful("Organization creation successful.");

        ServiceUtil.configureResponseObserver(responseObserver, result);
    }

    @Override
    public void readOrganization(UserRequest request, StreamObserver<ReadOrganizationResult> responseObserver) {

        List<OrganizationEntity> allOrganizationEntities = organizationService.getAllOrganizations();

        List<Organization> allOrganizations = allOrganizationEntities.stream()
                .map(organizationEntity -> organizationEntity.parseEntity().getOrganization())
                .collect(Collectors.toList());

        ReadOrganizationResult readOrganizationRes = ReadOrganizationResult.newBuilder()
                .addAllOrganizations(allOrganizations).build();

        ServiceUtil.configureResponseObserver(responseObserver, readOrganizationRes);

    }

    @Override
    public void updateOrganization(UpdateOrganizationRequest request, StreamObserver<Result> responseObserver) {

        long organizationId = request.getOrganizationId();

        OrganizationEntity organizationEntity;

        try {
            organizationEntity = organizationRepository
                    .findById(organizationId)
                    .orElseThrow(IllegalArgumentException::new);
        } catch (IllegalArgumentException illegalArgumentException) {
            Result result = ServiceUtil.returnError("Cannot find organization from given ID.");
            ServiceUtil.configureResponseObserver(responseObserver, result);
            return;
        }

        organizationRepository.delete(organizationEntity);

        Organization organization = request.getOrganization();

        OrganizationEntity organizationEntity2 = EntityParser.parseOrganization(organization);

        organizationRepository.save(organizationEntity2);

        Result result = returnSuccessful("Organization update successful.");

        configureResponseObserver(responseObserver, result);
    }

    @Override
    public void deleteOrganization(DeleteOrganizationRequest request, StreamObserver<Result> responseObserver) {

        long organizationId = request.getOrganizationId();

        OrganizationEntity organizationEntity;

        try {
            organizationEntity = organizationRepository
                    .findById(organizationId)
                    .orElseThrow(IllegalArgumentException::new);
        } catch (IllegalArgumentException illegalArgumentException) {
            Result result = returnError("Cannot delete organization from given ID.");
            configureResponseObserver(responseObserver, result);
            return;
        }

        organizationRepository.delete(organizationEntity);

        Result result = returnSuccessful("Organization deletion successful.");

        configureResponseObserver(responseObserver, result);
    }
}