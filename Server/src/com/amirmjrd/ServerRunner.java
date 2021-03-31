package com.amirmjrd;

import com.amirmjrd.server.Server;

import java.io.IOException;

public class ServerRunner {
    public static Server server;

    static {
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        server.start();
    }
}
