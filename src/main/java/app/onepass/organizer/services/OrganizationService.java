package app.onepass.organizer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.Empty;

import app.onepass.apis.AssignRoleRequest;
import app.onepass.apis.CreateOrganizationRequest;
import app.onepass.apis.GetObjectByIdRequest;
import app.onepass.apis.GetOrganizationsResponse;
import app.onepass.apis.HasPermissionRequest;
import app.onepass.apis.Organization;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.Permission;
import app.onepass.apis.RemoveOrganizationRequest;
import app.onepass.apis.Role;
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
	public void createOrganization(CreateOrganizationRequest request, StreamObserver<Organization> responseObserver) {

		int organizationId = request.getOrganization().getId();

		int userId = request.getUserId();

		if (organizationRepository.findById(organizationId).isPresent()) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "An organization with this ID already exists.");

			return;
		}

		OrganizationMessage organizationMessage = new OrganizationMessage(request.getOrganization());

		ServiceUtil.saveEntity(organizationMessage, organizationRepository);

		UserOrganizationEntity userOrganizationEntity = UserOrganizationEntity.builder()
				.userId(userId)
				.organizationId(organizationId)
				.build();

		userOrganizationRepository.save(userOrganizationEntity);

		AssignRoleRequest assignRoleRequest = AssignRoleRequest.newBuilder()
				.setUserId(userId)
				.setOrganizationId(organizationId)
				.setRole(Role.ORGANIZATION_OWNER)
				.build();

		accountService.assignRole(assignRoleRequest);

		ServiceUtil.returnObject(responseObserver, request.getOrganization());
	}

	@Override
	public void getOrganizations(Empty request, StreamObserver<GetOrganizationsResponse> responseObserver) {

		List<OrganizationEntity> allOrganizationEntities = organizationRepository.findAll();

		List<Organization> allOrganizations = allOrganizationEntities.stream()
				.map(organizationEntity -> organizationEntity.parseEntity().getOrganization())
				.collect(Collectors.toList());

		GetOrganizationsResponse getOrganizationResponse = GetOrganizationsResponse.newBuilder()
				.addAllOrganizations(allOrganizations)
				.build();

		ServiceUtil.returnObject(responseObserver, getOrganizationResponse);
	}

	@Override
	public void getOrganizationById(GetObjectByIdRequest request, StreamObserver<Organization> responseObserver) {

		OrganizationEntity organizationEntity;

		try {

			organizationEntity = organizationRepository.findById(request.getId()).orElseThrow(IllegalArgumentException::new);

		} catch (IllegalArgumentException illegalArgumentException) {

			ServiceUtil.returnObject(responseObserver, Organization.newBuilder().build());

			return;
		}

		Organization organization = organizationEntity.parseEntity().getOrganization();

		ServiceUtil.returnObject(responseObserver, organization);
	}

	@Override
	public void updateOrganization(UpdateOrganizationRequest request, StreamObserver<Organization> responseObserver) {

		HasPermissionRequest hasPermissionRequest = ServiceUtil.createHasPermissionRequest(request.getUserId(),
				request.getOrganization().getId(), Permission.ORGANIZATION_UPDATE);

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

		ServiceUtil.returnObject(responseObserver, request.getOrganization());
	}

	@Override
	public void removeOrganization(RemoveOrganizationRequest request, StreamObserver<Organization> responseObserver) {

		HasPermissionRequest hasPermissionRequest = ServiceUtil.createHasPermissionRequest(request.getUserId(),
				request.getOrganizationId(), Permission.ORGANIZATION_REMOVE);

		if (!accountService.hasPermission(hasPermissionRequest).getValue()) {

			ServiceUtil.returnPermissionDeniedError(responseObserver);

			return;
		}

		int organizationId = request.getOrganizationId();

		OrganizationEntity organizationEntity;

		try {

			organizationEntity = organizationRepository.findById(organizationId).orElseThrow(IllegalArgumentException::new);

		} catch (IllegalArgumentException illegalArgumentException) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "Cannot find organization from given ID.");

			return;
		}

		organizationRepository.delete(organizationEntity);

		ServiceUtil.returnObject(responseObserver, organizationEntity.parseEntity().getOrganization());
	}

	@Override
	public void addUsersToOrganization(UpdateUsersInOrganizationRequest request, StreamObserver<Empty> responseObserver) {

		HasPermissionRequest hasPermissionRequest = ServiceUtil.createHasPermissionRequest(request.getUserId(),
				request.getOrganizationId(), Permission.ORGANIZATION_MEMBER_ADD);

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

		HasPermissionRequest hasPermissionRequest = ServiceUtil.createHasPermissionRequest(request.getUserId(),
				request.getOrganizationId(), Permission.ORGANIZATION_MEMBER_REMOVE);

		if (!accountService.hasPermission(hasPermissionRequest).getValue()) {

			ServiceUtil.returnPermissionDeniedError(responseObserver);

			return;
		}

		int organizationId = request.getOrganizationId();

		List<Integer> userIds = request.getUserIdsList();

		List<UserOrganizationEntity> entitiesToDelete = new ArrayList<>();

		for (int userId : userIds) {

			UserOrganizationEntity userOrganizationEntity = userOrganizationRepository.findByUserIdAndOrganizationId(userId,
					organizationId);

			if (userOrganizationEntity != null) {

				entitiesToDelete.add(userOrganizationEntity);
			}
		}

		userOrganizationRepository.deleteAll(entitiesToDelete);

		ServiceUtil.returnEmpty(responseObserver);
	}
}