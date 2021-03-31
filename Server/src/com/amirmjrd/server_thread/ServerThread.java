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
import java.util.Objects;

public class ServerThread extends Thread implements IProtocol {
    private DataOutputStream writer;
    private DataInputStream reader;
    private Socket socket;
    private String username;
    private Server server;
    private String messageText;
    private Message message;
    private ClientSideParser clientSideParser;
    boolean exit;

    public ServerThread(Socket socket) throws IOException {
        this.exit = false;
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
        while (!exit) {
            try {
                messageText = receiveMessage();
                if (socket.isClosed()) break;
                Commands command = clientSideParser.findTypeOfMessage(messageText);
                this.message = clientSideParser.getMessage();
                switch (Objects.requireNonNull(command)) {
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
                        this.message.setUsername(this.username);
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
        try {
            this.socket.close();
            this.reader.close();
            this.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void handShake() {
        try {
            this.username = message.getUsername();
            if (!this.server.isExist(username)) {
                this.server.addClient(username, this);
                sendMessage(Messages.serverHandshakeToClient(username));
                this.server.sendMessageToAll(username);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getList() {
        try {
            ArrayList<String> usernames1 = new ArrayList<>(this.server.getClients().keySet());
            String usernames = this.server.generateUsernames(usernames1);
            this.sendMessage(Messages.serverGetList(usernames));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPublicMessage() {
        System.out.println("From public message: " + this.message.getBodyMessage());
        this.server.sendPublicMessage(this.message);
    }

    @Override
    public void sendPrivateMessage() {
        this.server.sendPrivateMessage(message);
    }


    @Override
    public void exit() {
        this.server.getClients().remove(username);
        this.server.getClients().forEach((s, serverThread) -> {
            try {
                serverThread.sendMessage(Messages.serverExit(username));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        try {
            this.reader.close();
            this.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
