package app.onepass.organizer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.protobuf.Empty;

import app.onepass.apis.CreateOrganizationRequest;
import app.onepass.apis.GetByIdRequest;
import app.onepass.apis.GetOrganizationByIdResponse;
import app.onepass.apis.GetOrganizationsResponse;
import app.onepass.apis.Organization;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.RemoveOrganizationRequest;
import app.onepass.apis.UpdateOrganizationRequest;
import app.onepass.apis.UpdateUsersInOrganizationRequest;
import app.onepass.organizer.entities.OrganizationEntity;
import app.onepass.organizer.entities.UserOrganizationEntity;
import app.onepass.organizer.messages.OrganizationMessage;
import app.onepass.organizer.repositories.OrganizationRepository;
import app.onepass.organizer.repositories.UserOrganizationRepository;
import app.onepass.organizer.utilities.ServiceUtil;
import io.grpc.stub.StreamObserver;

@Service
public class OrganizationService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserOrganizationRepository userOrganizationRepository;

    @Override
    @Transactional
    public void createOrganization(CreateOrganizationRequest request, StreamObserver<Empty> responseObserver) {

        if (organizationRepository.findById(request.getOrganization().getId()).isPresent()) {

            ServiceUtil.returnInvalidArgumentError(responseObserver, "An organization with this ID already exists.");

        }

        OrganizationMessage organizationMessage = new OrganizationMessage(request.getOrganization());

        ServiceUtil.saveEntity(organizationMessage, organizationRepository);

        responseObserver.onCompleted();
    }

    @Override
    public void getOrganizations(Empty request, StreamObserver<GetOrganizationsResponse> responseObserver) {

        List<OrganizationEntity> allOrganizationEntities = organizationRepository.findAll();

        List<Organization> allOrganizations = allOrganizationEntities.stream()
                .map(organizationEntity -> organizationEntity.parseEntity().getOrganization())
                .collect(Collectors.toList());

        GetOrganizationsResponse getOrganizationResult = GetOrganizationsResponse.newBuilder()
                .addAllOrganizations(allOrganizations).build();

        ServiceUtil.returnObject(responseObserver, getOrganizationResult);
    }

    @Override
    public void getOrganizationById(GetByIdRequest request, StreamObserver<GetOrganizationByIdResponse> responseObserver) {

        OrganizationEntity organizationEntity;

        try {

            organizationEntity = organizationRepository
                    .findById(request.getId())
                    .orElseThrow(IllegalArgumentException::new);

        } catch (IllegalArgumentException illegalArgumentException) {

            GetOrganizationByIdResponse getOrganizationByIdResponse = GetOrganizationByIdResponse
                    .newBuilder()
                    .build();

            ServiceUtil.returnObject(responseObserver, getOrganizationByIdResponse);

            return;
        }

        Organization organization = organizationEntity.parseEntity().getOrganization();

        GetOrganizationByIdResponse getOrganizationByIdResult = GetOrganizationByIdResponse
                .newBuilder()
                .setOrganization(organization)
                .build();

       ServiceUtil.returnObject(responseObserver, getOrganizationByIdResult);
    }

    @Override
    @Transactional
    public void updateOrganization(UpdateOrganizationRequest request, StreamObserver<Empty> responseObserver) {

        if (!organizationRepository.findById(request.getOrganization().getId()).isPresent()) {

            ServiceUtil.returnInvalidArgumentError(responseObserver, "An organization with this ID does not exist.");

            return;
        }

        OrganizationMessage organizationMessage = new OrganizationMessage(request.getOrganization());

        ServiceUtil.saveEntity(organizationMessage, organizationRepository);

        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void removeOrganization(RemoveOrganizationRequest request, StreamObserver<Empty> responseObserver) {

        long organizationId = request.getOrganizationId();

        boolean deleteSuccessful = ServiceUtil.deleteEntity(organizationId, organizationRepository);

        if (!deleteSuccessful) {

            ServiceUtil.returnInvalidArgumentError(responseObserver, "Cannot find organization from given ID.");

            return;
        }

        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void addUsersToOrganization(UpdateUsersInOrganizationRequest request, StreamObserver<Empty> responseObserver) {

        List<UserOrganizationEntity> userOrganizationEntities = new ArrayList<>();

        for (int index = 0; index < request.getUserIdsCount(); index++) {

            UserOrganizationEntity userOrganizationEntity = UserOrganizationEntity.builder()
                    .userId(request.getUserIds(index))
                    .organizationId(request.getOrganizationId())
                    .build();

            userOrganizationEntities.add(userOrganizationEntity);
        }

        userOrganizationRepository.saveAll(userOrganizationEntities);

        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void removeUsersFromOrganization(UpdateUsersInOrganizationRequest request, StreamObserver<Empty> responseObserver) {

        List<Long> userIds = request.getUserIdsList();

        List<UserOrganizationEntity> userOrganizationEntities = userOrganizationRepository
                .findByOrganizationId(request.getOrganizationId());

        List<UserOrganizationEntity> entitiesToDelete = new ArrayList<>();

        //TODO: Optimize!

        for (UserOrganizationEntity userOrganizationEntity : userOrganizationEntities) {

            for (Long userId : userIds) {

                if (userOrganizationEntity.getUserId() == userId) {

                    entitiesToDelete.add(userOrganizationEntity);
                }
            }
        }

        userOrganizationRepository.deleteAll(entitiesToDelete);

        responseObserver.onCompleted();
    }
}