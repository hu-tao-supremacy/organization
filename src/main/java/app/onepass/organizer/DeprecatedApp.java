package app.onepass.organizer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class DeprecatedApp {

	private static OrganizerImpl organizerImpl;

	@Autowired
	private DeprecatedApp(OrganizerImpl organizerImpl) {
		DeprecatedApp.organizerImpl = organizerImpl;
	}

	public static void main( String[] args ) throws IOException, InterruptedException {
		Server server = ServerBuilder.forPort(50053).addService(organizerImpl).build();
		server.start();
		server.awaitTermination();
	}
}
