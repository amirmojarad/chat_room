package com.amirmjrd.client;

import com.amirmjrd.Commands;
import com.amirmjrd.Messages;
import com.amirmjrd.interfaces.SuperClient;

import java.io.IOException;

public class Client extends SuperClient {

    public Client() throws IOException {
        super();
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
    public void privateMessage() {

    }

    @Override
    public void publicMessage() {

    }

    @Override
    public void getList() {

    }

    public String rec() throws IOException {
        this.receiveMessage();
        return messageText;
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
