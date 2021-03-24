package com.amirmjrd.client;

import com.amirmjrd.interfaces.ClientSide;
import com.amirmjrd.interfaces.SuperClient;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends SuperClient implements ClientSide {

    public Client() throws IOException {
        super();
    }

}
