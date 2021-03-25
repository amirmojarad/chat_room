import com.amirmjrd.client.Client;
import com.amirmjrd.interfaces.IProtocol;

import java.io.IOException;

public class ClientRunner {
    public static void main(String[] args) throws IOException {
        Client client = new Client("amirmjrd");
        client.start();
    }
}
