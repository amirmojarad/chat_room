package com.amirmjrd.interfaces;

import com.amirmjrd.parser.Message;
import com.amirmjrd.parser.ServerSideParser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class SuperClient extends Thread {
    protected DataInputStream reader;
    protected DataOutputStream writer;
    protected Socket socket;
    protected String username, messageText;
    protected ServerSideParser serverSideParser;
    protected Message message;
    protected ArrayList<String> selectedUsernames;
    private final int PORT = 21000;
    private final String ADDRESS = "localhost";

    public SuperClient() throws IOException {
        socket = new Socket(ADDRESS, PORT);
        reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        writer = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
        serverSideParser = new ServerSideParser();
        this.selectedUsernames = new ArrayList<>();
    }

    protected void sendMessage(String message) throws IOException {
        writer.writeUTF(message);
        writer.flush();
    }

    protected String getUsernames() {
        StringBuilder builder = new StringBuilder();
        selectedUsernames.forEach(s -> {
            builder.append(String.format("%s,", s));
        });
        return builder.toString();
    }

    protected boolean addUsernames(ArrayList<String> usernames) {
        if (this.selectedUsernames.containsAll(usernames)) {
            this.selectedUsernames.addAll(usernames);
            return true;
        }
        return false;
    }

    protected boolean addUsernames(String username) {
        if (!selectedUsernames.contains(username)) {
            this.selectedUsernames.add(username);
            return true;
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    protected void receiveMessage() throws IOException {
        this.messageText = reader.readUTF();
    }
}
