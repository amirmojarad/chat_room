package com.amirmjrd.server;

import com.amirmjrd.Messages;
import com.amirmjrd.interfaces.IProtocol;
import com.amirmjrd.interfaces.SuperServer;
import com.amirmjrd.server_thread.ServerThread;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server extends SuperServer {
    private HashMap<String, ServerThread> clients;

    public Server() throws IOException {
        super();
        this.clients = new HashMap<>();
    }

    private boolean isExist(String username) {
        return clients.containsKey(username);
    }

    public void sendMessageToAll(String username) {
        clients.forEach((clientUsername, serverThread) -> {
            if (!username.equals(clientUsername)) {
                try {
                    serverThread.sendMessage(Messages.serverHandshakeToAll(username));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void addClient(String username, ServerThread serverThread) throws IOException {
        if (!isExist(username)) {
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

}
