package app.onepass.organizer.services;

import org.springframework.stereotype.Service;

import app.onepass.apis.OrganizerServiceGrpc;

@Service
public class EventService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

//	@Autowired
//	private EventRepository eventRepository;
//
//	@Override
//	public void createOrganization(CreateOrganizationRequest request, StreamObserver<Result> responseObserver) {
//
//		OrganizationMessage organizationMessage = new OrganizationMessage(request.getOrganization());
//
//		ServiceUtil.saveEntity(organizationMessage, organizationRepository);
//
//		Result result = ServiceUtil.returnSuccessful("Organization creation successful.");
//
//		ServiceUtil.configureResponseObserver(responseObserver, result);
//	}
//
//	@Override
//	public void readOrganization(UserRequest request, StreamObserver<ReadOrganizationResult> responseObserver) {
//
//		List<OrganizationEntity> allOrganizationEntities = organizationRepository.findAll();
//
//		List<Organization> allOrganizations = allOrganizationEntities.stream()
//				.map(organizationEntity -> organizationEntity.parseEntity().getOrganization())
//				.collect(Collectors.toList());
//
//		ReadOrganizationResult readOrganizationRes = ReadOrganizationResult.newBuilder()
//				.addAllOrganizations(allOrganizations).build();
//
//		ServiceUtil.configureResponseObserver(responseObserver, readOrganizationRes);
//
//	}
//
//	@Override
//	public void readOrganizationById(ReadByIdRequest request, StreamObserver<ReadOrganizationByIdResult> responseObserver) {
//
//		OrganizationEntity organizationEntity;
//
//		try {
//
//			organizationEntity = organizationRepository
//					.findById(request.getReadId())
//					.orElseThrow(IllegalArgumentException::new);
//
//		} catch (IllegalArgumentException illegalArgumentException) {
//
//			ReadOrganizationByIdResult result = ReadOrganizationByIdResult
//					.newBuilder()
//					.setOrganization((Organization) null)
//					.build();
//
//			ServiceUtil.configureResponseObserver(responseObserver, result);
//
//			return;
//		}
//
//		Organization organization = organizationEntity.parseEntity().getOrganization();
//
//		ReadOrganizationByIdResult readOrganizationByIdResult = ReadOrganizationByIdResult
//				.newBuilder()
//				.setOrganization(organization)
//				.build();
//
//		ServiceUtil.configureResponseObserver(responseObserver, readOrganizationByIdResult);
//
//	}
//
//	@Override
//	public void updateOrganization(UpdateOrganizationRequest request, StreamObserver<Result> responseObserver) {
//
//		long organizationId = request.getOrganizationId();
//
//		boolean deleteSuccessful = ServiceUtil.deleteEntity(organizationId, organizationRepository);
//
//		if (!deleteSuccessful) {
//
//			Result result = ServiceUtil.returnError("Cannot find organization from given ID.");
//
//			ServiceUtil.configureResponseObserver(responseObserver, result);
//
//			return;
//
//		}
//
//		OrganizationMessage organizationMessage = new OrganizationMessage(request.getOrganization());
//
//		ServiceUtil.saveEntity(organizationMessage, organizationRepository);
//
//		Result result = ServiceUtil.returnSuccessful("Organization update successful.");
//
//		ServiceUtil.configureResponseObserver(responseObserver, result);
//	}
//
//	@Override
//	public void deleteOrganization(DeleteOrganizationRequest request, StreamObserver<Result> responseObserver) {
//
//		long organizationId = request.getOrganizationId();
//
//		boolean deleteSuccessful = ServiceUtil.deleteEntity(organizationId, organizationRepository);
//
//		if (!deleteSuccessful) {
//
//			Result result = ServiceUtil.returnError("Cannot find organization from given ID.");
//
//			ServiceUtil.configureResponseObserver(responseObserver, result);
//
//			return;
//
//		}
//
//		Result result = ServiceUtil.returnSuccessful("Organization deletion successful.");
//
//		ServiceUtil.configureResponseObserver(responseObserver, result);
//	}
//
//	@Override
//	public void updateEventFacility(UpdateEventFacilityRequest request, StreamObserver<Result> responseObserver) {
//		super.updateEventFacility(request, responseObserver);
//	}
//
//	@Override
//	public void updateEventDuration(UpdateEventDurationRequest request, StreamObserver<Result> responseObserver) {
//		super.updateEventDuration(request, responseObserver);
//	}
//
//	@Override
//	public void updateRegistrationRequest(UpdateRegistrationRequestRequest request, StreamObserver<Result> responseObserver) {
//		super.updateRegistrationRequest(request, responseObserver);
//	}
//
//	@Override
//	public void hasEvent(HasEventRequest request, StreamObserver<Result> responseObserver) {
//		super.hasEvent(request, responseObserver);
//	}
}
