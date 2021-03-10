package app.onepass.organizer.integration;

import org.springframework.stereotype.Service;

import com.google.protobuf.Empty;

import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.apis.Result;
import app.onepass.organizer.utilities.ServiceUtil;
import io.grpc.stub.StreamObserver;

@Service
public class PingService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Override
	public void ping(Empty request, StreamObserver<Result> responseObserver) {

		Result result = ServiceUtil.returnSuccessful("PONG!");

		ServiceUtil.configureResponseObserver(responseObserver, result);

	}
}
