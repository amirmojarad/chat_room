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
    // I/O components
    private final DataOutputStream writer;
    private final DataInputStream reader;
    // client socket
    private final Socket socket;
    private String username;
    private Server server;
    // message text
    private String messageText;
    // received and sent messages
    private Message message;
    private final ClientSideParser clientSideParser;
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

    /**
     * if socket is not closed, send message in UTF style and then flush it
     *
     * @param message send a message to a client
     * @throws IOException when I/O component does not open successfully
     */
    public void sendMessage(String message) throws IOException {
        if (socket.isClosed()) return;
        writer.writeUTF(message);
        writer.flush();
    }

    /**
     * if socket is not closed, read message as UTF and then return the message text
     *
     * @return message text as raw message from server
     * @throws IOException when I/O component does not open successfully
     */
    public String receiveMessage() throws IOException {
        if (!socket.isClosed())
            try {
                messageText = reader.readUTF();
            } catch (EOFException e) {
                this.socket.close();
            }
        return messageText;
    }

    /**
     * while server is not exit :
     *  - take message
     *  - if socket is not closed: resume
     *  - parse message and get the message from parser
     *  - switch between protocol Requests
     */
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

    /**
     * get username from message
     * if server has not same client with this username resume
     * add new client to server, send message to the client
     * and send to all online clients
     */
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

    /**
     * get usernames from clients in server, send message to client
     */
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

    /**
     * server send a public message to all clients
     */
    @Override
    public void sendPublicMessage() {
        this.server.sendPublicMessage(this.message);
    }

    /**
     * server sends a private message to specific clients
     */
    @Override
    public void sendPrivateMessage() {
        this.server.sendPrivateMessage(message, this);
    }

    /**
     * first remove this username, and then send to all clients goodbye message!
     */
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
