package app.onepass.organizer.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;

import app.onepass.apis.AccountServiceGrpc;
import app.onepass.apis.HasPermissionRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Service
public class AccountService {

	@Value("${HTS_SVC_ACCOUNT}")
	String address;

	private String getHost() {
		return address.split(":")[0];
	}

	private int getPort() {
		return Integer.parseInt(address.split(":")[1]);
	}

	public BoolValue ping() {

		ManagedChannel channel = ManagedChannelBuilder.forAddress(getHost(), getPort())
				.usePlaintext()
				.build();

		AccountServiceGrpc.AccountServiceBlockingStub stub = AccountServiceGrpc.newBlockingStub(channel);

		BoolValue response = stub.ping(Empty.newBuilder().build());

		channel.shutdown();

		return response;
	}

	public BoolValue hasPermission(HasPermissionRequest hasPermissionRequest) {

		ManagedChannel channel = ManagedChannelBuilder.forAddress(getHost(), getPort())
				.usePlaintext()
				.build();

		AccountServiceGrpc.AccountServiceBlockingStub stub = AccountServiceGrpc.newBlockingStub(channel);

		BoolValue response = stub.hasPermission(hasPermissionRequest);

		channel.shutdown();

		return response;
	}
}