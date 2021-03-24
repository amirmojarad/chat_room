package com.amirmjrd.interfaces;

import java.io.*;
import java.net.Socket;

public abstract class SuperClient {
    protected String inMessage, outMessage;
    protected DataOutputStream writer;
    protected DataInputStream reader;
    Socket socket;
    private final static int PORT = 21000;
    private final static String ADDRESS = "localhost";

    public SuperClient() throws IOException {
        socket = new Socket(ADDRESS, PORT);
        writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        inMessage = "";
        outMessage = "";
    }

    void writeMessage() throws IOException {
        writer.writeUTF(outMessage);
        writer.flush();
    }

    void readMessage() throws IOException {
        inMessage = reader.readUTF();
    }


}
