package com.amirmjrd.client;

import com.amirmjrd.Messages;
import com.amirmjrd.interfaces.IProtocol;
import com.amirmjrd.interfaces.SuperClient;

import java.io.IOException;

/**
 * implement methods of IProtocol
 * interface between SuperClient and outer world
 */
public class Client extends SuperClient implements IProtocol {
    public Client(String username) throws IOException {
        super();
        this.username = username;
        handShake();
    }

    /**
     * send message with content HANDSHAKE
     * by IProtocol
     */
    @Override
    public void handShake() {
        try {
            sendMessage(Messages.clientHandshake(username));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * send message with content EXIT
     * by IProtocol
     * <p>
     * closing socket and I/O Components
     */
    @Override
    public void exit() {
        try {
            sendMessage(Messages.clientExit());
            socket.close();
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * send message with content GET_LIST
     * by IProtocol
     */
    @Override
    public void getList() {
        try {
            sendMessage(Messages.clientGetList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * send message with content PUBLIC_MESSAGE
     * by IProtocol
     */
    @Override
    public void sendPublicMessage() {
        try {
            sendMessage(Messages.clientPublicMessage(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * send message with content PRIVATE_MESSAGE
     * by IProtocol
     */
    @Override
    public void sendPrivateMessage() {
        try {
            sendMessage(Messages.clientPrivateMessage(message, getUsernames()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * thread for running client side program
     * while exit not called resume to work
     * receiving messages
     * sending to parser
     * get the message
     * added it to message stack
     */
    @Override
    public void run() {
        while (!exit) {
            try {
                if (this.socket.isClosed()) break;
                receiveMessage();
                serverSideParser.findTypeOfMessage(messageText);
                message = serverSideParser.getMessage();
                System.out.println(message.getRawMessage());
                this.messages.add(this.message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
