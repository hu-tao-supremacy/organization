package app.onepass.organizer.services;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
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

	private final ManagedChannel channel;

	private final AccountServiceGrpc.AccountServiceBlockingStub stub;

	@Autowired
	public AccountService(@Value("${HTS_SVC_ACCOUNT}") String address) {

		channel = ManagedChannelBuilder.forAddress(getHost(address), getPort(address)).usePlaintext().build();

		stub = AccountServiceGrpc.newBlockingStub(channel);
	}

	private static String getHost(String address) {
		return address.split(":")[0];
	}

	private static int getPort(String address) {
		return Integer.parseInt(address.split(":")[1]);
	}

	@PreDestroy
	public void onDestroy() {
		channel.shutdown();
	}

	public BoolValue ping() {

		return stub.ping(Empty.newBuilder().build());
	}

	public BoolValue hasPermission(HasPermissionRequest hasPermissionRequest) {

		return stub.hasPermission(hasPermissionRequest);
	}
}