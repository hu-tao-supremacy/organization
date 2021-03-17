package app.onepass.organizer.utilities;

import java.util.function.BiConsumer;

import org.springframework.dao.DataAccessException;

import com.google.protobuf.GeneratedMessageV3;

import app.onepass.apis.Result;
import io.grpc.stub.StreamObserver;

public class DatabaseExceptionCatcher {

	public static <M extends GeneratedMessageV3> void
	catcher(BiConsumer<M, StreamObserver<Result>> consumer, M request, StreamObserver<Result> responseObserver) {

		try {

			consumer.accept(request, responseObserver);

		} catch (DataAccessException exception) {

			Result result = ServiceUtil.returnError("Unable to manipulate database.");

			ServiceUtil.configureResponseObserver(responseObserver, result);
		}
	}
}
