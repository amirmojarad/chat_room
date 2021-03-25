package com.amirmjrd.client;

import com.amirmjrd.Commands;
import com.amirmjrd.Messages;
import com.amirmjrd.interfaces.IProtocol;
import com.amirmjrd.interfaces.SuperClient;

import java.io.IOException;

public class Client extends SuperClient implements IProtocol {

    public Client(String username) throws IOException {
        super();
        this.username = username;
        handShake();
    }


    @Override
    public void handShake() {
        try {
            sendMessage(Messages.clientHandshake(username));
            receiveMessage();
            System.out.println(messageText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exit() {
        try {
            sendMessage(Messages.clientExit());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getList() {
        try {
            sendMessage(Messages.clientGetList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPublicMessage() {
        try {
            sendMessage(Messages.clientPublicMessage(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPrivateMessage() {
        try {
            sendMessage(Messages.clientPrivateMessage(message, getUsernames()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean exit = false;
        while (!exit) {
            try {
                receiveMessage();
                Commands commands = serverSideParser.findTypeOfMessage(messageText);
                message = serverSideParser.getMessage();
                switch (commands) {
                    case HANDSHAKE:
                        handShake();
                        break;
                    case GET_LIST:
                        getList();
                        break;
                    case PRIVATE_MESSAGE:
                        sendPrivateMessage();
                    case PUBLIC_MESSAGE:
                        sendPublicMessage();
                    case EXIT:
                        exit = true;
                        exit();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
