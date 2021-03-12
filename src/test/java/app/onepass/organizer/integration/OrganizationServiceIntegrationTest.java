package app.onepass.organizer.integration;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import app.onepass.apis.CreateOrganizationRequest;
import app.onepass.apis.Organization;
import app.onepass.apis.Result;
import app.onepass.apis.UserRequest;
import app.onepass.organizer.repositories.OrganizationRepository;
import io.grpc.internal.testing.StreamRecorder;

@SpringBootTest
public class OrganizationServiceIntegrationTest {

	private static final long USER_ID = 11;
	private static final long ORGANIZATION_ID_1 = 13;
	private static final long ORGANIZATION_ID_2 = 42;
	private static final long ORGANIZATION_ID_3 = 103;
	private static final String ORGANIZATION_NAME_1 = "ICE";
	private static final String ORGANIZATION_NAME_2 = "ESC";
	private static final boolean IS_VERIFIED = true;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private OrganizationService organizationService;

	@Test
	public void createOrganization() {

		Organization organization = buildOrganization(ORGANIZATION_ID_1, ORGANIZATION_NAME_1, IS_VERIFIED);

		CreateOrganizationRequest createOrganizationRequest = buildCreateOrganizationRequest(USER_ID, organization);

		StreamRecorder<Result> responseObserver = StreamRecorder.create();

		organizationService.createOrganization(createOrganizationRequest, responseObserver);

		Result response = getLatestNonErrorResponse(responseObserver);

		assertTrue(response.getIsOk());

		assertEquals(response.getDescription(), "Organization creation successful.");

		assertTrue(organizationRepository.findById(ORGANIZATION_ID_1).isPresent());
	}

	@Test
	public void createOrganizationWithSameId() {

		Organization organization1 = buildOrganization(ORGANIZATION_ID_1, ORGANIZATION_NAME_1, IS_VERIFIED);
		Organization organization2 = buildOrganization(ORGANIZATION_ID_1, ORGANIZATION_NAME_2, IS_VERIFIED);

		CreateOrganizationRequest createOrganizationRequest1 = buildCreateOrganizationRequest(USER_ID, organization1);
		CreateOrganizationRequest createOrganizationRequest2 = buildCreateOrganizationRequest(USER_ID, organization2);

		StreamRecorder<Result> responseObserver = StreamRecorder.create();

		organizationService.createOrganization(createOrganizationRequest1, responseObserver);
		organizationService.createOrganization(createOrganizationRequest2, responseObserver);

		Result response = getLatestNonErrorResponse(responseObserver);

		assertFalse(response.getIsOk());

		assertEquals(response.getDescription(), "Unable to create organization.");

		assertTrue(organizationRepository.findById(ORGANIZATION_ID_1).isPresent());
	}

	private static UserRequest buildUserRequest(long userId) {
		return UserRequest.newBuilder()
				.setUserId(userId)
				.build();
	}

	private static Organization buildOrganization(long organizationId, String organizationName, boolean isVerified) {
		return Organization.newBuilder()
				.setId(organizationId)
				.setName(organizationName)
				.setIsVerified(isVerified)
				.build();
	}

	private static CreateOrganizationRequest buildCreateOrganizationRequest(long userId, Organization organization) {
		return CreateOrganizationRequest.newBuilder()
				.setUserId(userId)
				.setOrganization(organization)
				.build();
	}

	private <T> T getLatestNonErrorResponse(StreamRecorder<T> responseObserver) {

		assertNull(responseObserver.getError());

		List<T> responses = responseObserver.getValues();

		return responses.get(responses.size() - 1);
	}
}
