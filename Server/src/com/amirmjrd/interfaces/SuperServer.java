package com.amirmjrd.interfaces;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class SuperServer extends Thread {
    protected ServerSocket serverSocket;
    protected final int PORT = 21000;
    protected boolean serverIsActive;

    public SuperServer() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.serverIsActive = !this.serverSocket.isClosed();
    }
}
