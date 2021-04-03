package com.amirmjrd;

import com.amirmjrd.parser.Message;

public class Messages {

    //////////////////////////////////////////////////////
    //                  HANDSHAKE                       //
    //////////////////////////////////////////////////////

    /**
     * @param username get this and set it to string
     * @return Client Handshake
     */
    public static String clientHandshake(String username) {
        return String.format("Hello %s", username);
    }

    /**
     * @param username set it to string
     * @return Server Handshake
     */
    public static String serverHandshakeToClient(String username) {
        return String.format("Hi %s, welcome to the chat room.", username);
    }

    /**
     * @param username set it to string
     * @return Server Handshake
     */
    public static String serverHandshakeToAll(String username) {
        return String.format("%s join the chat room.", username);
    }

    //////////////////////////////////////////////////////
    //                    PRIVATE                       //
    //////////////////////////////////////////////////////

    /**
     * @param message   get message as data structure
     * @param usernames comma separated usernames
     * @return server side Private message
     */
    public static String serverPrivateMessage(Message message, String usernames) {
        return String.format("Private message, length = %d from %s to %s : \r\n%s",
                message.getMessageLength(),
                message.getUsername(),
                usernames,
                message.getBodyMessage());

    }

    /**
     * @param message   get message as data structure
     * @param usernames comma separated usernames
     * @return client side Private message
     */
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

    /**
     * @param message get message as data structure
     * @return server side Private message
     */
    public static String serverPublicMessage(Message message) {
        //Public message from <username> , length = <message_length> : \r\n<message_body>
        return String.format("Public message from %s , length = %d : \r\n%s",
                message.getUsername(),
                message.getMessageLength(),
                message.getBodyMessage());
    }

    /**
     * @param message get message as data structure
     * @return client side Private message
     */
    public static String clientPublicMessage(Message message) {
        return String.format("Public message, length = %d : \r\n%s", message.getMessageLength(), message.getBodyMessage());
    }

    //////////////////////////////////////////////////////
    //                    GET_LIST                      //
    //////////////////////////////////////////////////////

    /**
     * @param usernames comma separated usernames
     * @return server side Private message
     */
    public static String serverGetList(String usernames) {
        return String.format("Here is the list of attendees : \r\n%s", usernames);
    }

    /**
     * @return client side Private message
     */
    public static String clientGetList() {
        return "Please send the list of attendees.";
    }

    //////////////////////////////////////////////////////
    //                    EXIT                          //
    //////////////////////////////////////////////////////

    /**
     * client say goodbye to room!
     * @return
     */
    public static String clientExit() {
        return "Bye.";
    }

    /**
     * server accept goodbye
     * @param username
     * @return
     */
    public static String serverExit(String username) {
        return String.format("%s left the chatroom", username);
    }
}
