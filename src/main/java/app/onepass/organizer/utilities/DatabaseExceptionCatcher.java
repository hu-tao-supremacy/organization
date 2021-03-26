package app.onepass.organizer.utilities;

import java.util.function.BiConsumer;

import org.springframework.dao.DataAccessException;

import com.google.protobuf.Empty;
import com.google.protobuf.GeneratedMessageV3;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class DatabaseExceptionCatcher {

	public static <M extends GeneratedMessageV3> void
	catcher(BiConsumer<M, StreamObserver<Empty>> consumer, M request, StreamObserver<Empty> responseObserver) {

		try {

			consumer.accept(request, responseObserver);

		} catch (DataAccessException exception) {

			responseObserver.onError(Status.UNAVAILABLE
					.withDescription("Unable to manipulate database.")
					.asException());

			responseObserver.onCompleted();
		}
	}
}
