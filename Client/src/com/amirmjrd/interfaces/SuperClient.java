package com.amirmjrd.interfaces;

import com.amirmjrd.Commands;
import com.amirmjrd.parser.Message;
import com.amirmjrd.parser.ServerSideParser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Client inner functions, methods and attributes
 */

public abstract class SuperClient extends Thread {
    // read messages from server
    protected DataInputStream reader;
    // send messages to server
    protected DataOutputStream writer;
    protected Socket socket;
    // hold username of client and received message
    protected String username, messageText;
    // parser for SERVER SIDE messages
    protected ServerSideParser serverSideParser;
    // received message
    protected Message message;
    // selected usernames in private message
    protected ArrayList<String> selectedUsernames;
    // hold messages in stack (messages from server)
    protected Stack<Message> messages;
    // if exit called, this value turned to true
    protected boolean exit;

    /**
     * default constructor for client program
     *
     * @throws IOException when I/O components opened successfully
     */
    public SuperClient() throws IOException {
        exit = false;
        // opened socket in "localhost" server address and in "21000" port
        socket = new Socket("localhost", 21000);
        reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        writer = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
        serverSideParser = new ServerSideParser();
        this.selectedUsernames = new ArrayList<>();
        this.messages = new Stack<>();
    }

    /**
     * write message as UTF and flush
     *
     * @param message
     * @throws IOException when I/O components not opened correct
     */
    protected void sendMessage(String message) throws IOException {
        writer.writeUTF(message);
        writer.flush();
    }

    /**
     * @return username arraylist as string separated by ,(comma)
     */
    protected String getUsernames() {
        StringBuilder builder = new StringBuilder();
        selectedUsernames.forEach(s -> builder.append(String.format("%s,", s)));
        return builder.toString();
    }

    /**
     * pop message from Stack
     *
     * @return newest message sent from server
     */
    public Message getLastMessage() {
        if (!messages.isEmpty())
            return this.messages.pop();
        return null;
    }

    /**
     * if socket is open, receive message from server
     * @throws IOException
     */
    public void receiveMessage() throws IOException {
        if (exit) return;
        if (!socket.isClosed())
            this.messageText = reader.readUTF();
    }

    /**
     * @param rawMessage get raw message for set into message attribute
     * @param commands get command type and set into message attribute
     */
    public void setMessage(String rawMessage, Commands commands) {
        this.message = new Message(rawMessage, commands);
        this.message.setUsername(this.username);
        this.message.setBodyMessage(rawMessage);
    }

    /**
     * @return true when stack message is empty
     */
    public boolean isEmpty() {
        return this.messages.isEmpty();
    }

    /**
     *
      * @param selectedUsernames get usernames from view model and set into usernames arraylist
     */
    public void setSelectedUsernames(ArrayList<String> selectedUsernames) {
        this.selectedUsernames = selectedUsernames;
    }
}
