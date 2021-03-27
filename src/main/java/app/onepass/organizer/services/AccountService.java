package app.onepass.organizer.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

// 	@Value("${GRPC_HOST}")
// 	String host;

// 	@Value("${HTS_SVC_ACCOUNT}")
// 	int port;

//	public Result ping() {
//		ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
//				.usePlaintext()
//				.build();
//
//		AccountServiceGrpc.AccountServiceBlockingStub stub = AccountServiceGrpc.newBlockingStub(channel);
//
////		HasPermissionInput request = HasPermissionInput.newBuilder()
////				.setUserId(1)
////				.setOrganizationId(1)
////				.setPermissionName(Permission.CREATE_TAG)
////				.build();
//
//		Result result = stub.ping(Empty.newBuilder().build());
//
//		channel.shutdown();
//
//		return result;
//	}
}
