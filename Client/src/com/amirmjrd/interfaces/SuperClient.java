package com.amirmjrd.interfaces;

import com.amirmjrd.parser.Message;
import com.amirmjrd.parser.ServerSideParser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class SuperClient extends Thread {
    protected DataInputStream reader;
    protected DataOutputStream writer;
    protected Socket socket;
    protected String username, messageText;
    protected ServerSideParser serverSideParser;
    protected Message message;
    private final int PORT = 21000;
    private final String ADDRESS = "localhost";

    public SuperClient() throws IOException {
        socket = new Socket(ADDRESS, PORT);
        reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        writer = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
        serverSideParser = new ServerSideParser();
    }

    protected void sendMessage(String message) throws IOException {
        writer.writeUTF(message);
        writer.flush();
    }

    protected void receiveMessage() throws IOException {
        this.messageText = reader.readUTF();
    }

    public abstract void handShake();

    public abstract void exit();

    public abstract void privateMessage();

    public abstract void publicMessage();

    public abstract void getList();
}
