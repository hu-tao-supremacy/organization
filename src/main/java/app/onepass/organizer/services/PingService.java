package app.onepass.organizer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;

import app.onepass.apis.AccountServiceGrpc;
import app.onepass.apis.HasPermissionRequest;
import app.onepass.apis.OrganizerServiceGrpc;
import app.onepass.organizer.utilities.ServiceUtil;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

@Service
public class PingService extends OrganizerServiceGrpc.OrganizerServiceImplBase {

	@Autowired
	private AccountService accountService;

	@Override
	public void ping(Empty request, StreamObserver<BoolValue> responseObserver) {

		Channel channel = ManagedChannelBuilder.forAddress("localhost", 50055).usePlaintext().build();

		AccountServiceGrpc.AccountServiceBlockingStub stub = AccountServiceGrpc.newBlockingStub(channel);

		HasPermissionRequest request2 = HasPermissionRequest.newBuilder()
				.setUserId(5)
				.setOrganizationId(21)
				.setPermissionNameValue(0)
				.build();
		BoolValue a = stub.hasPermission(request2);
		ServiceUtil.returnObject(responseObserver, a);
		//ServiceUtil.returnObject(responseObserver, BoolValue.of(true));
	}
}
