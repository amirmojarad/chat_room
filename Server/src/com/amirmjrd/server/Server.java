package com.amirmjrd.server;

import com.amirmjrd.Messages;
import com.amirmjrd.interfaces.SuperServer;
import com.amirmjrd.parser.Message;
import com.amirmjrd.server_thread.ServerThread;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Server extends SuperServer {
    private HashMap<String, ServerThread> clients;
    public Server() throws IOException {
        super();
        this.clients = new HashMap<>();
    }

    public boolean isExist(String username) {
        return clients.containsKey(username);
    }

    public void sendMessageToAll(String username) {

        clients.forEach((clientUsername, serverThread) -> {
//            if (!username.equals(clientUsername)) {
            try {
                serverThread.sendMessage(Messages.serverHandshakeToAll(username));
            } catch (IOException e) {
                e.printStackTrace();
//                }
            }
        });
    }

    public void addClient(String username, ServerThread serverThread) throws IOException {
        if (!isExist(username)) {
            clients.forEach((s, serverThread1) -> System.out.println(s));
            this.clients.putIfAbsent(username, serverThread);
        }
    }

    public HashMap<String, ServerThread> getClients() {
        return clients;
    }

    @Override
    public void run() {
        while (serverIsActive) {
            try {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket, this);
                serverThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendPublicMessage(Message message) {
        clients.forEach((s, serverThread) -> {
            try {
                serverThread.sendMessage(Messages.serverPublicMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendPrivateMessage(Message message) {
        ArrayList<String> usernames = message.getUsernames();
        this.clients.forEach((s, serverThread) -> {
            if (usernames.contains(s)) {
                String messageText = Messages.serverPrivateMessage(message, generateUsernames(usernames));
                try {
                    serverThread.sendMessage(messageText);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String generateUsernames(Collection<String> usernames) {
        List<String> strings = (List<String>) usernames;
        StringBuilder builder = new StringBuilder();
        this.clients.keySet().forEach(s -> {
            if (strings.contains(s))
                builder.append(String.format("%s,", s));
        });
        return builder.toString();
    }
}
