import com.amirmjrd.client.Client;

import java.io.IOException;
import java.util.Scanner;

public class ClientRunner {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        boolean exit = false;
        Scanner input = new Scanner(System.in);
        while (!exit){
            System.out.println("Enter a command: ");
            String message = input.nextLine();
            switch (message){
                case "handshake":
                    client.handShake();
                    break;
                case "exit":
                    exit = true;
                    client.exit();
                    break;
            }
        }
//        client.start();
    }
}
