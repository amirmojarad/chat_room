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
        if (message.contains("\r\n")) {
            split("\r\n");
            if (this.tokensList.contains("Private")) {
                split("\r\n");
                String[] usernames = tokensList.get(4).split(",");
                ArrayList<String> usernamesList = new ArrayList<>(Arrays.asList(usernames));

//                PrivateMessage privateMessage = new PrivateMessage(usernamesList, );
                return Commands.PRIVATE_MESSAGE;
            } else if (this.tokensList.contains("Public"))
                return Commands.PUBLIC_MESSAGE;
        } else {
            split("\\s+");
            if (this.tokensList.contains("Hello")) {
                String username = message.split("\\s+")[1];
                this.message = new Message(message, username, Commands.HANDSHAKE);
                return Commands.HANDSHAKE;
            } else if (this.tokensList.contains("list"))
                return Commands.GET_LIST;
            else if (this.tokensList.contains("Bye.")){

                return Commands.EXIT;
            }
        }
        return Commands.NULL;
    }
}
