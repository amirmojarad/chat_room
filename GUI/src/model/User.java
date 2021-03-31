package model;

import com.amirmjrd.client.Client;

import java.io.IOException;

public class User {
    private Client client;
    private String username, port, fullName, password;

    public User(String username, String port, String fullName, String password) {
        this.username = username;
        this.port = port;
        this.fullName = fullName;
        this.password = password;
        try {
            this.client = new Client(this.username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client getClient() {
        return client;
    }


    public String getUsername() {
        return username;
    }


}
