package com.amirmjrd;

import com.amirmjrd.parser.Message;

public class Messages {

    //////////////////////////////////////////////////////
    //                  HANDSHAKE                       //
    //////////////////////////////////////////////////////
    public static String clientHandshake(String username) {
        return String.format("Hello %s", username);
    }

    public static String serverHandshakeToClient(String username) {
        return String.format("Hi %s, welcome to the chat room.", username);
    }

    public static String serverHandshakeToAll(String username) {
        return String.format("%s join the chat room.", username);
    }

    //////////////////////////////////////////////////////
    //                    PRIVATE                       //
    //////////////////////////////////////////////////////
    public static String serverPrivateMessage(Message message, String usernames) {
        return String.format("Private message, length = %d from %s to %s : \r\n%s",
                message.getMessageLength(),
                message.getUsername(),
                usernames,
                message.getBodyMessage());

    }

    public static String clientPrivateMessage(Message message, String usernames) {
        return String.format("Private message, length = %d from %s to %s : \r\n%s",
                message.getMessageLength(),
                message.getUsername(),
                usernames,
                message.getBodyMessage());
    }

    //////////////////////////////////////////////////////
    //                    PUBLIC                        //
    //////////////////////////////////////////////////////
    public static String serverPublicMessage(Message message) {
        //Public message from <username> , length = <message_length> : \r\n<message_body>
        return String.format("Public message from %s , length = %d : \r\n%s",
                message.getUsername(),
                message.getMessageLength(),
                message.getBodyMessage());
    }

    public static String clientPublicMessage(Message message) {
        return String.format("Public message, length = %d : \r\n%s", message.getMessageLength(), message.getBodyMessage());
    }

    //////////////////////////////////////////////////////
    //                    GET_LIST                      //
    //////////////////////////////////////////////////////
    public static String serverGetList(String usernames) {
        return String.format("Here is the list of attendees : \r\n%s", usernames);
    }

    public static String clientGetList() {
        return "Please send the list of attendees.";
    }

    //////////////////////////////////////////////////////
    //                    EXIT                          //
    //////////////////////////////////////////////////////
    public static String clientExit() {
        return "Bye.";
    }

    public static String serverExit(String username) {
        return String.format("%s left the chatroom", username);
    }
}
