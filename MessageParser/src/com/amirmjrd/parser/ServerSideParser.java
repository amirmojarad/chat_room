package com.amirmjrd.parser;

import com.amirmjrd.Commands;

import java.util.ArrayList;
import java.util.Arrays;

public class ServerSideParser extends Parser {
    String messageBody;

    @Override
    public Commands findTypeOfMessage(String message) {
        this.messageText = message;
        split("\\s+");
        if (message.contains("\r\n")) {
            messageBody = message.substring(message.indexOf("\n"));
            messageBody = messageBody.trim();
            if (this.tokensList.contains("Here"))
                return getList();
            else if (this.tokensList.contains("Public"))
                publicMessage();
            else if (this.tokensList.contains("Private"))
                privateMessage();
        } else {
            if (this.tokensList.contains("join"))
                join();
            else if (this.tokensList.contains("Hi"))
                return handshake();
            else if (this.tokensList.contains("left"))
                return exit();
        }
        return Commands.NULL;
    }

    public Commands getList() {
        this.message = new Message(messageText, Commands.GET_LIST);
        this.message.setUsernames(splitUsernames(tokensList.get(7)));
        return Commands.GET_LIST;
    }


    private Commands publicMessage() {
        this.message = new Message(messageText, Commands.PUBLIC_MESSAGE);
        this.message.setUsername(this.tokensList.get(3));
        this.message.setMessageLength(Integer.parseInt(this.tokensList.get(7)));
        this.message.setBodyMessage(messageBody);
        return Commands.PUBLIC_MESSAGE;
    }

    private Commands privateMessage() {
        this.message = new Message(messageText, Commands.PRIVATE_MESSAGE);
        this.message.setUsername(this.tokensList.get(6));
        this.message.setMessageLength(Integer.parseInt(this.tokensList.get(4)));
        this.message.setUsernames(splitUsernames(this.tokensList.get(8)));
        this.message.setBodyMessage(messageBody);
        return Commands.PRIVATE_MESSAGE;
    }

    private Commands join() {
        this.message = new Message(messageText, Commands.HANDSHAKE);
        this.message.setUsername(this.tokensList.get(0));
        return Commands.HANDSHAKE;
    }

    private Commands handshake() {
        this.message = new Message(messageText, Commands.HANDSHAKE);
        this.message.setUsername(this.tokensList.get(1));
        return Commands.HANDSHAKE;
    }


    private Commands exit() {
        this.message = new Message(messageText, Commands.EXIT);
        this.message.setUsername(this.tokensList.get(0));
        return Commands.EXIT;
    }

    private ArrayList<String> splitUsernames(String usernames) {
        System.out.println(usernames);
        String[] usernamesStrings = usernames.split(",");
        return new ArrayList<>(Arrays.asList(usernamesStrings));
    }
}
