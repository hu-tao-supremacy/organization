package app.onepass.organizer.integration;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
	private static final long ORGANIZATION_ID = 13;
	private static final String ORGANIZATION_NAME = "ICE";
	private static final boolean IS_VERIFIED = true;

	private static final UserRequest USER_REQUEST = UserRequest.newBuilder()
			.setUserId(USER_ID)
			.build();

	private static final Organization ORGANIZATION = Organization.newBuilder()
			.setId(ORGANIZATION_ID)
			.setName(ORGANIZATION_NAME)
			.setIsVerified(IS_VERIFIED)
			.build();

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private OrganizationService organizationService;

	@Test
	public void createOrganization() {

		CreateOrganizationRequest createOrganizationRequest = CreateOrganizationRequest.newBuilder()
				.setUserId(USER_ID)
				.setOrganization(ORGANIZATION)
				.build();

		StreamRecorder<Result> responseObserver = StreamRecorder.create();

		organizationService.createOrganization(createOrganizationRequest, responseObserver);

		assertNull(responseObserver.getError());

		List<Result> responses = responseObserver.getValues();

		assertEquals(1, responses.size());

		Result response = responses.get(0);

		assertTrue(response.getIsOk());

		assertEquals(response.getDescription(), "Organization creation successful.");

		assertTrue(organizationRepository.findById(ORGANIZATION_ID).isPresent());
	}
}
