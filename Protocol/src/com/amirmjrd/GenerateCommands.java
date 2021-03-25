package com.amirmjrd;

public class GenerateCommands {
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Commands generateCommand(String command) {
        this.command = command;
        switch (command) {
            case "handshake":
                return Commands.HANDSHAKE;
            case "getList":
                return Commands.GET_LIST;
            case "exit":
                return Commands.EXIT;
            case "publicMessage":
                return Commands.PUBLIC_MESSAGE;
            case "privateMessage":
                return Commands.PRIVATE_MESSAGE;
        }
        return Commands.NULL;
    }


}
