package app.onepass.organizer.utilities;

import java.util.function.BiConsumer;

import com.google.protobuf.GeneratedMessageV3;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class ExceptionCatcher {

	public static <M extends GeneratedMessageV3, R extends GeneratedMessageV3> void catcher(BiConsumer<M, StreamObserver<R>> consumer, M request,
			StreamObserver<R> responseObserver) {

		try {

			consumer.accept(request, responseObserver);

//		} catch (DataAccessException exception) {
//
//			responseObserver.onError(Status.UNAVAILABLE.withDescription("Unable to manipulate database.").asException());

		} catch (StatusRuntimeException exception) {

			responseObserver.onError(Status.UNAVAILABLE.withDescription("Exception occurred on other services.").asException());

		}
	}
}
