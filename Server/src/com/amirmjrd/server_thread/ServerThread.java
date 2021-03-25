package com.amirmjrd.server_thread;

import com.amirmjrd.Commands;
import com.amirmjrd.Messages;
import com.amirmjrd.parser.ClientSideParser;
import com.amirmjrd.parser.Message;
import com.amirmjrd.server.Server;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerThread extends Thread {
    private DataOutputStream writer;
    private DataInputStream reader;
    private Socket socket;
    private String username;
    private Server server;
    private String message;
    ClientSideParser clientSideParser;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        this.writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        this.reader = new DataInputStream(new DataInputStream(socket.getInputStream()));
        this.clientSideParser = new ClientSideParser();
    }

    public ServerThread(Socket socket, Server server) throws IOException {
        this(socket);
        this.server = server;
    }

    public ServerThread(Socket socket, String username) throws IOException {
        this(socket);
        this.username = username;
    }

    public void sendMessage(String message) throws IOException {
        if (socket.isClosed()) return;
        writer.writeUTF(message);
        writer.flush();
    }

    public String receiveMessage() throws IOException {
        try {
            message = reader.readUTF();
        } catch (EOFException e) {
            this.socket.close();
        }
        return message;
    }

    @Override
    public void run() {
        boolean exit = false;
        while (!exit) {
            try {
                message = receiveMessage();
                Commands command = clientSideParser.findTypeOfMessage(message);
                Message message = clientSideParser.getMessage();
                switch (command) {
                    case HANDSHAKE:
                        this.username = message.getUsername();
                        this.server.addClient(username, this);
                        sendMessage(Messages.serverHandshakeToClient(username));
                        this.server.sendMessageToAll(username);
                        break;
                    case EXIT:
                        this.socket.close();
                        this.server.getClients().remove(username);
                        this.server.getClients().forEach((s, serverThread) -> {
                            try {
                                serverThread.sendMessage(Messages.serverExit(username));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        exit = true;
                        break;
                    default:
                        exit = true;
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
