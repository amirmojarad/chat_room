package com.amirmjrd.server_thread;

import com.amirmjrd.Commands;
import com.amirmjrd.Messages;
import com.amirmjrd.interfaces.IProtocol;
import com.amirmjrd.parser.ClientSideParser;
import com.amirmjrd.parser.Message;
import com.amirmjrd.server.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread implements IProtocol {
    private DataOutputStream writer;
    private DataInputStream reader;
    private Socket socket;
    private String username;
    private Server server;
    private String messageText;
    private Message message;
    private ClientSideParser clientSideParser;

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
            messageText = reader.readUTF();
        } catch (EOFException e) {
            this.socket.close();
        }
        return messageText;
    }

    @Override
    public void run() {
        boolean exit = false;
        while (!exit) {
            try {
                messageText = receiveMessage();
                Commands command = clientSideParser.findTypeOfMessage(messageText);
                this.message = clientSideParser.getMessage();
                switch (command) {
                    case HANDSHAKE:
                        handShake();
                        break;
                    case EXIT:
                        exit();
                        exit = true;
                        break;
                    case GET_LIST:
                        getList();
                        break;
                    case PRIVATE_MESSAGE:
                        sendPrivateMessage();
                        break;
                    case PUBLIC_MESSAGE:
                        sendPublicMessage();
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


    @Override
    public void handShake() {
        try {
            this.username = message.getUsername();
            this.server.addClient(username, this);
            sendMessage(Messages.serverHandshakeToClient(username));
            this.server.sendMessageToAll(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getList() {
        try {
            this.sendMessage(Messages.serverGetList(this.server.generateUsernames(message.getUsernames())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPublicMessage() {
        this.server.sendPublicMessage(this.message);
    }

    @Override
    public void sendPrivateMessage() {
        this.server.sendPrivateMessage(message);
    }


    @Override
    public void exit() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.server.getClients().remove(username);
        this.server.getClients().forEach((s, serverThread) -> {
            try {
                serverThread.sendMessage(Messages.serverExit(username));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
