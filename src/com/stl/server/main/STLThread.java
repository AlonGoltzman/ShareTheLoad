package com.stl.server.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * A thread designed to handle incoming connections before a pair are connected.
 */
public class STLThread extends Thread {

    //============ Variables [START]

    private Socket user_socket;

    //============ Variables [END]

    public STLThread(Socket connection) {
        user_socket = connection;
    }

    @Override
    public void run() {

        // Check what is wanted from the socket.
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(user_socket.getInputStream()));

            String request = reader.readLine();

            if("host".equalsIgnoreCase(request)){}
                

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
