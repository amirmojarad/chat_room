package com.amirmjrd.parser;

import com.amirmjrd.Commands;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientSideParser extends Parser {
    public ClientSideParser() {
    }

    @Override
    public Commands findTypeOfMessage(String message) {
        this.messageText = message;
        //Multi-Line Messages
        split("\\s+");
        if (message.contains("\r\n")) {
            if (this.tokensList.contains("Private")) {
                privateMessage();
                return Commands.PRIVATE_MESSAGE;
            } else if (this.tokensList.contains("Public")) {
                publicMessage();
                return Commands.PUBLIC_MESSAGE;
            }
        } //Single-Line Messages
        else {
            if (this.tokensList.contains("Hello")) {
                hello();
                return Commands.HANDSHAKE;
            } else if (this.tokensList.contains("list"))
                // find the list keyword means protocol message parsing is done
                return Commands.GET_LIST;
            else if (this.tokensList.contains("Bye."))
                return Commands.EXIT;
        }
        return Commands.NULL;
    }

    private void publicMessage() {
        this.message = new Message(messageText, Commands.PUBLIC_MESSAGE);
        this.message.setMessageLength(Integer.parseInt(tokensList.get(4)));
        this.message.setBodyMessage(tokensList.get(6));
    }

    private void privateMessage() {
        String[] usernames = tokensList.get(8).split(",");
        ArrayList<String> usernamesList = new ArrayList<>(Arrays.asList(usernames));
        this.message = new Message(messageText, Commands.PRIVATE_MESSAGE);
        this.message.setUsernames(usernamesList);
        this.message.setBodyMessage(tokensList.get(10));
        this.message.setMessageLength(Integer.parseInt(tokensList.get(4)));
        this.message.setUsername(tokensList.get(6));
    }

    private void hello() {
        this.message = new Message(this.messageText, Commands.HANDSHAKE);
        this.message.setUsername(messageText.split("\\s+")[1]);
    }


}
