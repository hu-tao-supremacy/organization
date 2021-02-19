package app.onepass.organizer;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException
    {
        Server server = ServerBuilder.forPort(50053).addService(new OrganizerImpl()).build();
        server.start();
        server.awaitTermination();
    }
}
