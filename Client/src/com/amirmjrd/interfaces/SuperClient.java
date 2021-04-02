package com.amirmjrd.interfaces;

import com.amirmjrd.Commands;
import com.amirmjrd.parser.Message;
import com.amirmjrd.parser.ServerSideParser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

public abstract class SuperClient extends Thread {
    protected DataInputStream reader;
    protected DataOutputStream writer;
    protected Socket socket;
    protected String username, messageText;
    protected ServerSideParser serverSideParser;
    protected Message message;
    protected ArrayList<String> selectedUsernames;
    protected Stack<Message> messages;
    protected boolean exit;
    protected boolean newMessage = false;
    public SuperClient() throws IOException {
        exit = false;
        socket = new Socket("localhost", 21000);
        reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        writer = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
        serverSideParser = new ServerSideParser();
        this.selectedUsernames = new ArrayList<>();
        this.messages = new Stack<>();
    }

    protected void sendMessage(String message) throws IOException {
        writer.writeUTF(message);
        writer.flush();
    }



    protected String getUsernames() {
        StringBuilder builder = new StringBuilder();
        selectedUsernames.forEach(s -> builder.append(String.format("%s,", s)));
        return builder.toString();
    }

    public Message getLastMessage() {
        if (!messages.isEmpty()){
            newMessage = false;
            return this.messages.pop();
        }
        return null;
    }


    public void receiveMessage() throws IOException {
        if (exit) return;
        if (!socket.isClosed()) {
            this.messageText = reader.readUTF();
            newMessage = true;
        }
    }

    public void setMessage(String rawMessage, Commands commands) {
        this.message = new Message(rawMessage, commands);
        this.message.setUsername(this.username);
        this.message.setBodyMessage(rawMessage);
    }

    public boolean isEmpty(){
        return this.messages.isEmpty();
    }

    public void setSelectedUsernames(ArrayList<String> selectedUsernames) {
        this.selectedUsernames = selectedUsernames;
    }
}
