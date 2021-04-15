package app.onepass.organizer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.Empty;

import app.onepass.apis.CreateLocationRequest;
import app.onepass.apis.GetObjectByIdRequest;
import app.onepass.apis.GetObjectByNameRequest;
import app.onepass.apis.Location;
import app.onepass.apis.LocationListResponse;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.RemoveLocationRequest;
import app.onepass.apis.UpdateLocationRequest;
import io.grpc.stub.StreamObserver;

@Service
public class LocationService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Autowired
	private AccountService accountService;

	@Override
	public void createLocation(CreateLocationRequest request, StreamObserver<Location> responseObserver) {
		super.createLocation(request, responseObserver);
	}

	@Override
	public void getLocations(Empty request, StreamObserver<LocationListResponse> responseObserver) {
		super.getLocations(request, responseObserver);
	}

	@Override
	public void getLocationById(GetObjectByIdRequest request, StreamObserver<Location> responseObserver) {
		super.getLocationById(request, responseObserver);
	}

	@Override
	public void searchLocationsByName(GetObjectByNameRequest request, StreamObserver<LocationListResponse> responseObserver) {
		super.searchLocationsByName(request, responseObserver);
	}

	@Override
	public void updateLocation(UpdateLocationRequest request, StreamObserver<Location> responseObserver) {
		super.updateLocation(request, responseObserver);
	}

	@Override
	public void removeLocation(RemoveLocationRequest request, StreamObserver<Location> responseObserver) {
		super.removeLocation(request, responseObserver);
	}
}

