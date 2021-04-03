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
    // holds clients
    private HashMap<String, ServerThread> clients;
    // create and run server socket in super()
    public Server() throws IOException {
        super();
        this.clients = new HashMap<>();
    }
    // if a username is in clients collection
    public boolean isExist(String username) {
        return clients.containsKey(username);
    }
    // take username and send public message
    public void sendMessageToAll(String username) {
        clients.forEach((clientUsername, serverThread) -> {
            try {
                serverThread.sendMessage(Messages.serverHandshakeToAll(username));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * get params and add it to clients
     * @param username client's username
     * @param serverThread client specific server thread
     */
    // get a username and server thread add a client to clients
    public void addClient(String username, ServerThread serverThread){
        if (!isExist(username)) {
            this.clients.putIfAbsent(username, serverThread);
        }
    }

    /**
     *
     * @return clients
     */
    public HashMap<String, ServerThread> getClients() {
        return clients;
    }

    /**
     * accept new client and assign a server thread to it then start
     */
    @Override
    public void run() {
        while (serverIsActive) {
            try {
                // server is active and waiting for clients
                Socket socket = serverSocket.accept();
                // after a clients arrive, create a server thread and pass this server and accepted socket to it
                ServerThread serverThread = new ServerThread(socket, this);
                // then start server thread in new thread
                serverThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param message get a message and send it to all clients
     */
    public void sendPublicMessage(Message message) {
        clients.forEach((s, serverThread) -> {
            try {
                serverThread.sendMessage(Messages.serverPublicMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * check usernames and send message to them and also the sender client
     * @param message get a message and send it to specific clients (that are in message.usernames)
     * @param thisServerThread sender client's server thread
     */
    public void sendPrivateMessage(Message message, ServerThread thisServerThread) {
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
        try {
            String messageText = Messages.serverPrivateMessage(message, generateUsernames(usernames));
            thisServerThread.sendMessage(messageText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get usernames and convert it to comma separated string usernames
     * @param usernames private usernames
     * @return comma separated usernames
     */
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
