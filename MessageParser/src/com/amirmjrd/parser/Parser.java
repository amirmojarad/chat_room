package com.amirmjrd.parser;

import com.amirmjrd.Commands;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Parser {
    protected String messageText = "";
    protected String[] tokens = null;
    protected ArrayList<String> tokensList;
    protected Message message = null;
    public Parser() {
        this.tokensList = new ArrayList<>();
    }

    protected int findMessageLength(String messageLength) {
        String length = messageLength.substring(1);
        length = length.substring(0, length.length() - 1);
        return Integer.parseInt(length);
    }



    protected void split(String splitter) {
        tokens = messageText.split(splitter);
        tokensList = new ArrayList<>(Arrays.asList(tokens));
    }

    protected abstract Commands findTypeOfMessage(String message);

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public static void main(String[] args) {
        ClientSideParser parser= new ClientSideParser();
        parser.findTypeOfMessage("Hello amir");

    }
//    protected abstract String ()
}
