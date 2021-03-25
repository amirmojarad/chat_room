import com.amirmjrd.server.Server;

import java.io.IOException;

public class ServerRunner {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}
