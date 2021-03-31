package com.amirmjrd.interfaces;

import java.util.ArrayList;

public class ReceivedMessage {
    ArrayList<String> interactions;
    ArrayList<String> publicMessages;
    ArrayList<String> privateMessages;

    public ReceivedMessage() {
        this.interactions = new ArrayList<>();
        this.publicMessages = new ArrayList<>();
        this.privateMessages = new ArrayList<>();
    }



}
