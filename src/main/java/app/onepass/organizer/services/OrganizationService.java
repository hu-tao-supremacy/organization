package app.onepass.organizer.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.onepass.apis.CreateOrganizationRequest;
import app.onepass.apis.DeleteOrganizationRequest;
import app.onepass.apis.Organization;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.ReadByIdRequest;
import app.onepass.apis.ReadOrganizationByIdResult;
import app.onepass.apis.ReadOrganizationResult;
import app.onepass.apis.Result;
import app.onepass.apis.UpdateOrganizationRequest;
import app.onepass.apis.UserRequest;
import app.onepass.organizer.entities.OrganizationEntity;
import app.onepass.organizer.messages.OrganizationMessage;
import app.onepass.organizer.repositories.OrganizationRepository;
import app.onepass.organizer.utilities.ServiceUtil;
import io.grpc.stub.StreamObserver;

@Service
public class OrganizationService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public void createOrganization(CreateOrganizationRequest request, StreamObserver<Result> responseObserver) {

        OrganizationMessage organizationMessage = new OrganizationMessage(request.getOrganization());

        ServiceUtil.saveEntity(organizationMessage, organizationRepository);

        Result result = ServiceUtil.returnSuccessful("Organization creation successful.");

        ServiceUtil.configureResponseObserver(responseObserver, result);
    }

    @Override
    public void readOrganization(UserRequest request, StreamObserver<ReadOrganizationResult> responseObserver) {

        List<OrganizationEntity> allOrganizationEntities = organizationRepository.findAll();

        List<Organization> allOrganizations = allOrganizationEntities.stream()
                .map(organizationEntity -> organizationEntity.parseEntity().getOrganization())
                .collect(Collectors.toList());

        ReadOrganizationResult readOrganizationRes = ReadOrganizationResult.newBuilder()
                .addAllOrganizations(allOrganizations).build();

        ServiceUtil.configureResponseObserver(responseObserver, readOrganizationRes);

    }

    @Override
    public void readOrganizationById(ReadByIdRequest request, StreamObserver<ReadOrganizationByIdResult> responseObserver) {

        OrganizationEntity organizationEntity;

        try {

            organizationEntity = organizationRepository
                    .findById(request.getReadId())
                    .orElseThrow(IllegalArgumentException::new);

        } catch (IllegalArgumentException illegalArgumentException) {

            ReadOrganizationByIdResult result = ReadOrganizationByIdResult
                    .newBuilder()
                    .setOrganization((Organization) null)
                    .build();

            ServiceUtil.configureResponseObserver(responseObserver, result);

            return;
        }

        Organization organization = organizationEntity.parseEntity().getOrganization();

        ReadOrganizationByIdResult readOrganizationByIdResult = ReadOrganizationByIdResult
                .newBuilder()
                .setOrganization(organization)
                .build();

        ServiceUtil.configureResponseObserver(responseObserver, readOrganizationByIdResult);

    }

    @Override
    public void updateOrganization(UpdateOrganizationRequest request, StreamObserver<Result> responseObserver) {

        long organizationId = request.getOrganizationId();

        boolean deleteSuccessful = ServiceUtil.deleteEntity(organizationId, organizationRepository);

        if (!deleteSuccessful) {

            Result result = ServiceUtil.returnError("Cannot find organization from given ID.");

            ServiceUtil.configureResponseObserver(responseObserver, result);

            return;

        }

        OrganizationMessage organizationMessage = new OrganizationMessage(request.getOrganization());

        ServiceUtil.saveEntity(organizationMessage, organizationRepository);

        Result result = ServiceUtil.returnSuccessful("Organization update successful.");

        ServiceUtil.configureResponseObserver(responseObserver, result);
    }

    @Override
    public void deleteOrganization(DeleteOrganizationRequest request, StreamObserver<Result> responseObserver) {

        long organizationId = request.getOrganizationId();

        boolean deleteSuccessful = ServiceUtil.deleteEntity(organizationId, organizationRepository);

        if (!deleteSuccessful) {

            Result result = ServiceUtil.returnError("Cannot find organization from given ID.");

            ServiceUtil.configureResponseObserver(responseObserver, result);

            return;

        }

        Result result = ServiceUtil.returnSuccessful("Organization deletion successful.");

        ServiceUtil.configureResponseObserver(responseObserver, result);
    }
}