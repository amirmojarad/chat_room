package com.amirmjrd.parser;

import com.amirmjrd.Commands;

public class Message {
    private String rawMessage, username, bodyMessage;
    private Commands commands;
    private int messageLength;

    public Message(String rawMessage, Commands commands) {
        this.rawMessage = rawMessage;
        this.commands = commands;
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

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
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

    public Commands getCommands() {
        return commands;
    }

    public void setCommands(Commands commands) {
        this.commands = commands;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }
}
