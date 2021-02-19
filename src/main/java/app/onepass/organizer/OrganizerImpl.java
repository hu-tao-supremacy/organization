package app.onepass.organizer;

import app.onepass.apis.AccountServiceGrpc;
import app.onepass.apis.HasPermissionRequest;
import app.onepass.apis.Result;

public class OrganizerImpl extends AccountServiceGrpc.AccountServiceImplBase {
	@Override
	public void hasPermission(HasPermissionRequest request, io.grpc.stub.StreamObserver<Result> responseObserver) {
		responseObserver.onNext(
			Result.newBuilder().setIsOk(true).setDescription("Jean").build()
							   );
		responseObserver.onCompleted();
		//		super.hasPermission(request, responseObserver);
	}
}
