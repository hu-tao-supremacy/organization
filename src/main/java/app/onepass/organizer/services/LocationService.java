package app.onepass.organizer.services;

import java.util.List;
import java.util.stream.Collectors;

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
import app.onepass.organizer.entities.LocationEntity;
import app.onepass.organizer.messages.LocationMessage;
import app.onepass.organizer.repositories.LocationRepository;
import app.onepass.organizer.utilities.ServiceUtil;
import io.grpc.stub.StreamObserver;

@Service
public class LocationService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Autowired
	LocationRepository locationRepository;

	@Override
	public void createLocation(CreateLocationRequest request, StreamObserver<Location> responseObserver) {

		if (locationRepository.existsById(request.getLocation().getId())) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "A location with this ID already exists.");

			return;
		}

		LocationMessage locationMessage = new LocationMessage(request.getLocation());

		LocationEntity savedEntity = locationRepository.save(locationMessage.parseMessage());

		ServiceUtil.returnObject(responseObserver, savedEntity.parseEntity().getLocation());
	}

	@Override
	public void getLocations(Empty request, StreamObserver<LocationListResponse> responseObserver) {

		List<LocationEntity> allLocationEntities = locationRepository.findAll();

		List<Location> allLocations = allLocationEntities.stream()
				.map(locationEntity -> locationEntity.parseEntity().getLocation())
				.collect(Collectors.toList());

		LocationListResponse getLocationResponse = LocationListResponse.newBuilder()
				.addAllLocations(allLocations)
				.build();

		ServiceUtil.returnObject(responseObserver, getLocationResponse);
	}

	@Override
	public void getLocationById(GetObjectByIdRequest request, StreamObserver<Location> responseObserver) {

		LocationEntity locationEntity;

		try {

			locationEntity = locationRepository.findById(request.getId()).orElseThrow(IllegalArgumentException::new);

		} catch (IllegalArgumentException illegalArgumentException) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "There is no location with the specified ID.");

			return;
		}

		Location location = locationEntity.parseEntity().getLocation();

		ServiceUtil.returnObject(responseObserver, location);
	}

	@Override
	public void searchLocationsByName(GetObjectByNameRequest request, StreamObserver<LocationListResponse> responseObserver) {

		List<LocationEntity> searchResults = locationRepository.findByName(request.getName());

		List<Location> allLocations = searchResults.stream()
				.map(locationEntity -> locationEntity.parseEntity().getLocation())
				.collect(Collectors.toList());

		LocationListResponse getLocationResponse = LocationListResponse.newBuilder()
				.addAllLocations(allLocations)
				.build();

		ServiceUtil.returnObject(responseObserver, getLocationResponse);
	}

	@Override
	public void updateLocation(UpdateLocationRequest request, StreamObserver<Location> responseObserver) {

		if (!locationRepository.existsById(request.getLocation().getId())) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "A location with this ID does not exist.");

			return;
		}

		LocationMessage locationMessage = new LocationMessage(request.getLocation());

		LocationEntity savedEntity = locationRepository.save(locationMessage.parseMessage());

		ServiceUtil.returnObject(responseObserver, savedEntity.parseEntity().getLocation());

	}

	@Override
	public void removeLocation(RemoveLocationRequest request, StreamObserver<Location> responseObserver) {

		int locationId = request.getLocationId();

		LocationEntity locationEntity;

		try {

			locationEntity = locationRepository.findById(locationId).orElseThrow(IllegalArgumentException::new);

		} catch (IllegalArgumentException illegalArgumentException) {

			ServiceUtil.returnInvalidArgumentError(responseObserver, "Cannot find location from given ID.");

			return;
		}

		locationRepository.delete(locationEntity);

		ServiceUtil.returnObject(responseObserver, locationEntity.parseEntity().getLocation());
	}
}

