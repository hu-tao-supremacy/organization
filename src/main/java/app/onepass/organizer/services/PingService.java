package app.onepass.organizer.services;

import org.springframework.stereotype.Service;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;

import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.organizer.utilities.ServiceUtil;
import io.grpc.stub.StreamObserver;

@Service
public class PingService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Override
	public void ping(Empty request, StreamObserver<BoolValue> responseObserver) {

		ServiceUtil.returnObject(responseObserver, BoolValue.of(true));
	}
}
