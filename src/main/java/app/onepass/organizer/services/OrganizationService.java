package app.onepass.organizer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.Empty;

import app.onepass.apis.CreateOrganizationRequest;
import app.onepass.apis.GetObjectByIdRequest;
import app.onepass.apis.HasPermissionRequest;
import app.onepass.apis.Organization;
import app.onepass.apis.OrganizationListResponse;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.Permission;
import app.onepass.apis.RemoveOrganizationRequest;
import app.onepass.apis.UpdateOrganizationRequest;
import app.onepass.apis.UpdateUsersInOrganizationRequest;
import app.onepass.apis.UserOrganization;
import app.onepass.apis.UserOrganizationListResponse;
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

		if (organizationRepository.findById(organizationId).isPresent()) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "An organization with this ID already exists.");

			return;
		}

		OrganizationMessage organizationMessage = new OrganizationMessage(request.getOrganization());

		OrganizationEntity savedEntity = organizationRepository.save(organizationMessage.parseMessage());

		UserOrganizationEntity userOrganizationEntity = UserOrganizationEntity.builder()
				.userId(request.getUserId())
				.organizationId(savedEntity.getId())
				.build();

		userOrganizationRepository.save(userOrganizationEntity);

		ServiceUtil.returnObject(responseObserver, savedEntity.parseEntity().getOrganization());
	}

	@Override
	public void getOrganizations(Empty request, StreamObserver<OrganizationListResponse> responseObserver) {

		List<OrganizationEntity> allOrganizationEntities = organizationRepository.findAll();

		List<Organization> allOrganizations = allOrganizationEntities.stream()
				.map(organizationEntity -> organizationEntity.parseEntity().getOrganization())
				.collect(Collectors.toList());

		OrganizationListResponse getOrganizationResponse = OrganizationListResponse.newBuilder()
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

		OrganizationEntity savedEntity = organizationRepository.save(organizationMessage.parseMessage());

		ServiceUtil.returnObject(responseObserver, savedEntity.parseEntity().getOrganization());
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
	public void addUsersToOrganization(UpdateUsersInOrganizationRequest request, StreamObserver<UserOrganizationListResponse> responseObserver) {

		HasPermissionRequest hasPermissionRequest = ServiceUtil.createHasPermissionRequest(request.getUserId(),
				request.getOrganizationId(), Permission.ORGANIZATION_MEMBER_ADD);

		if (!accountService.hasPermission(hasPermissionRequest).getValue()) {

			ServiceUtil.returnPermissionDeniedError(responseObserver);

			return;
		}

		List<UserOrganizationEntity> entitiesToAdd = new ArrayList<>();

		for (int index = 0; index < request.getUserIdsCount(); index++) {

			UserOrganizationEntity userOrganizationEntity = UserOrganizationEntity.builder()
					.userId(request.getUserIds(index))
					.organizationId(request.getOrganizationId())
					.build();

			entitiesToAdd.add(userOrganizationEntity);
		}

		List<UserOrganizationEntity> addedEntities = userOrganizationRepository.saveAll(entitiesToAdd);

		List<UserOrganization> userOrganizations = addedEntities.stream()
				.map(UserOrganizationEntity -> UserOrganizationEntity.parseEntity().getUserOrganization())
				.collect(Collectors.toList());

		UserOrganizationListResponse userOrganizationListResponse = UserOrganizationListResponse.newBuilder()
				.addAllUserOrganizations(userOrganizations)
				.build();

		ServiceUtil.returnObject(responseObserver, userOrganizationListResponse);
	}

	@Override
	public void removeUsersFromOrganization(UpdateUsersInOrganizationRequest request, StreamObserver<UserOrganizationListResponse> responseObserver) {

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

		List<UserOrganization> UserOrganizations = entitiesToDelete.stream()
				.map(UserOrganizationEntity -> UserOrganizationEntity.parseEntity().getUserOrganization())
				.collect(Collectors.toList());

		UserOrganizationListResponse userOrganizationListResponse = UserOrganizationListResponse.newBuilder()
				.addAllUserOrganizations(UserOrganizations)
				.build();

		ServiceUtil.returnObject(responseObserver, userOrganizationListResponse);
	}
}
