package com.amirmjrd.parser;

import com.amirmjrd.Commands;

import java.util.ArrayList;

public class Message {
    private String rawMessage, username, bodyMessage;
    private Commands commands;
    private int messageLength;
    private ArrayList<String> usernames;

    public Message(String rawMessage, Commands commands) {
        this.rawMessage = rawMessage;
        this.commands = commands;
        this.messageLength = this.rawMessage.length();
        this.usernames = new ArrayList<>();
    }

    public Message(String rawMessage, String username, Commands commands) {
        this(rawMessage, commands);
        this.username = username;
    }

    public Message(String rawMessage, String username, String bodyMessage, Commands commands, int messageLength) {
        this(rawMessage, username, commands);
        this.bodyMessage = bodyMessage;
        this.messageLength = messageLength;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBodyMessage() {
        return bodyMessage;
    }

    public void setBodyMessage(String bodyMessage) {
        this.bodyMessage = bodyMessage;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public Commands getCommands() {
        return commands;
    }

    public void setUsernames(ArrayList<String> usernames) {
        this.usernames = usernames;
    }
}
