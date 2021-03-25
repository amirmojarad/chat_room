package com.amirmjrd;

public class Messages {

    //////////////////////////////////////////////////////
    //                  HANDSHAKE                       //
    //////////////////////////////////////////////////////
    public static String clientHandshake(String username){
        return String.format("Hello %s", username);
    }

    public static String serverHandshakeToClient(String username){
        return String.format("Hi %s, welcome to the chat room.", username);
    }

    public static String serverHandshakeToAll(String username){
        return String.format("%s join the chat room.", username);
    }

    //////////////////////////////////////////////////////
    //                    EXIT                          //
    //////////////////////////////////////////////////////
    public static String clientExit(){
        return "Bye.";
    }

    public static String serverExit(String username){
        return String.format("%s left the chatroom", username);
    }
}
