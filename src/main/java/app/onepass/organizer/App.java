package app.onepass.organizer;

import java.io.IOException;

import org.springframework.stereotype.Component;

import app.onepass.organizer.utilities.StaticContextAccessor;
import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * Hello world!
 *
 */
@Component
public class App {

    public static void main( String[] args ) throws IOException, InterruptedException {
        OrganizerImpl organizerImpl = StaticContextAccessor.getBean(OrganizerImpl.class);

        Server server = ServerBuilder.forPort(50053).addService(organizerImpl).build();
        server.start();
        server.awaitTermination();
    }
}
