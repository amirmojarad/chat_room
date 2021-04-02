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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
//            receiveMessage();
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
        while (!exit) {
            try {
                if(this.socket.isClosed()) break;
                receiveMessage();
                Commands commands = serverSideParser.findTypeOfMessage(messageText);
                message = serverSideParser.getMessage();
                this.messages.add(this.message);
                if(commands.equals(Commands.PRIVATE_MESSAGE))
                    System.out.println("in Run: "+message.getBodyMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
