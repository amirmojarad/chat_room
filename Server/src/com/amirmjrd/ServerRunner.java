package com.amirmjrd;

import com.amirmjrd.server.Server;

import java.io.IOException;

public class ServerRunner {
    public static Server server;

    public static void main(String[] args) throws IOException {
server = new Server();
        server.start();
    }
}
