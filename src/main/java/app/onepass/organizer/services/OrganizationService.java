package app.onepass.organizer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.Empty;

import app.onepass.apis.CreateOrganizationRequest;
import app.onepass.apis.GetByIdRequest;
import app.onepass.apis.GetOrganizationByIdResponse;
import app.onepass.apis.GetOrganizationsResponse;
import app.onepass.apis.HasPermissionRequest;
import app.onepass.apis.Organization;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.Permission;
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
    private AccountService accountService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserOrganizationRepository userOrganizationRepository;

    @Override
    public void createOrganization(CreateOrganizationRequest request, StreamObserver<Empty> responseObserver) {

        if (organizationRepository.findById(request.getOrganization().getId()).isPresent()) {

            ServiceUtil.returnInvalidArgumentError(responseObserver, "An organization with this ID already exists.");

            return;
        }

        OrganizationMessage organizationMessage = new OrganizationMessage(request.getOrganization());

        ServiceUtil.saveEntity(organizationMessage, organizationRepository);

        ServiceUtil.returnEmpty(responseObserver);
    }

    @Override
    public void getOrganizations(Empty request, StreamObserver<GetOrganizationsResponse> responseObserver) {

        List<OrganizationEntity> allOrganizationEntities = organizationRepository.findAll();

        List<Organization> allOrganizations = allOrganizationEntities.stream()
                .map(organizationEntity -> organizationEntity.parseEntity().getOrganization())
                .collect(Collectors.toList());

        GetOrganizationsResponse getOrganizationResponse = GetOrganizationsResponse.newBuilder()
                .addAllOrganizations(allOrganizations).build();

        ServiceUtil.returnObject(responseObserver, getOrganizationResponse);
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

        GetOrganizationByIdResponse getOrganizationByIdResponse = GetOrganizationByIdResponse
                .newBuilder()
                .setOrganization(organization)
                .build();

       ServiceUtil.returnObject(responseObserver, getOrganizationByIdResponse);
    }

    @Override
    public void updateOrganization(UpdateOrganizationRequest request, StreamObserver<Empty> responseObserver) {

        HasPermissionRequest hasPermissionRequest = ServiceUtil.createHasPermissionRequest(request.getUserId(), request.getOrganization().getId(), Permission.ORGANIZATION_UPDATE);

        if (!accountService.hasPermission(hasPermissionRequest).getValue()) {

            ServiceUtil.returnPermissionDeniedError(responseObserver);

            return;
        }

        if (!organizationRepository.findById(request.getOrganization().getId()).isPresent()) {

            ServiceUtil.returnInvalidArgumentError(responseObserver, "An organization with this ID does not exist.");

            return;
        }

        OrganizationMessage organizationMessage = new OrganizationMessage(request.getOrganization());

        ServiceUtil.saveEntity(organizationMessage, organizationRepository);

        ServiceUtil.returnEmpty(responseObserver);
    }

    @Override
    public void removeOrganization(RemoveOrganizationRequest request, StreamObserver<Empty> responseObserver) {

        HasPermissionRequest hasPermissionRequest = ServiceUtil.createHasPermissionRequest(request.getUserId(), request.getOrganizationId(), Permission.ORGANIZATION_REMOVE);

        if (!accountService.hasPermission(hasPermissionRequest).getValue()) {

            ServiceUtil.returnPermissionDeniedError(responseObserver);

            return;
        }

        long organizationId = request.getOrganizationId();

        boolean deleteSuccessful = ServiceUtil.deleteEntity(organizationId, organizationRepository);

        if (!deleteSuccessful) {

            ServiceUtil.returnInvalidArgumentError(responseObserver, "Cannot find organization from given ID.");

            return;
        }

        ServiceUtil.returnEmpty(responseObserver);
    }

    @Override
    public void addUsersToOrganization(UpdateUsersInOrganizationRequest request, StreamObserver<Empty> responseObserver) {

        HasPermissionRequest hasPermissionRequest = ServiceUtil.createHasPermissionRequest(request.getUserId(), request.getOrganizationId(), Permission.ORGANIZATION_MEMBER_ADD);

        if (!accountService.hasPermission(hasPermissionRequest).getValue()) {

            ServiceUtil.returnPermissionDeniedError(responseObserver);

            return;
        }

        List<UserOrganizationEntity> userOrganizationEntities = new ArrayList<>();

        for (int index = 0; index < request.getUserIdsCount(); index++) {

            UserOrganizationEntity userOrganizationEntity = UserOrganizationEntity.builder()
                    .userId(request.getUserIds(index))
                    .organizationId(request.getOrganizationId())
                    .build();

            userOrganizationEntities.add(userOrganizationEntity);
        }

        userOrganizationRepository.saveAll(userOrganizationEntities);

        ServiceUtil.returnEmpty(responseObserver);
    }

    @Override
    public void removeUsersFromOrganization(UpdateUsersInOrganizationRequest request, StreamObserver<Empty> responseObserver) {

        HasPermissionRequest hasPermissionRequest = ServiceUtil.createHasPermissionRequest(request.getUserId(), request.getOrganizationId(), Permission.ORGANIZATION_MEMBER_REMOVE);

        if (!accountService.hasPermission(hasPermissionRequest).getValue()) {

            ServiceUtil.returnPermissionDeniedError(responseObserver);

            return;
        }

        long organizationId = request.getOrganizationId();

        List<Long> userIds = request.getUserIdsList();

        List<UserOrganizationEntity> entitiesToDelete = new ArrayList<>();

        for (long userId : userIds) {

            UserOrganizationEntity userOrganizationEntity = userOrganizationRepository
                    .findByUserIdAndOrganizationId(userId, organizationId);

            if (userOrganizationEntity != null) {

                entitiesToDelete.add(userOrganizationEntity);
            }
        }

        userOrganizationRepository.deleteAll(entitiesToDelete);

        ServiceUtil.returnEmpty(responseObserver);
    }
}