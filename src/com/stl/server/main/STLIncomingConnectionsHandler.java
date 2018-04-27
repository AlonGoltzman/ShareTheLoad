package com.stl.server.main;

import com.stl.server.commons.STLLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main server socket.
 *
 * @author Alon
 */
public class STLIncomingConnectionsHandler extends Thread {

    //============ Singleton [START]

    public static STLIncomingConnectionsHandler getInstance() throws IOException {
        return instance == null ? (instance = new STLIncomingConnectionsHandler()) : instance;
    }

    private static STLIncomingConnectionsHandler instance;

    private STLIncomingConnectionsHandler() throws IOException {
        logger = STLLogger.getInstance();
        try {
            serverSocket = new ServerSocket(15833);
        } catch (IOException e) {
            logger.fatal(e);
            throw e;
        }
    }

    //============ Singleton [END]

    //============ Variables [START]


    /**
     * The server socket, that is listening to incoming connections.
     */
    private ServerSocket serverSocket;

    /**
     * The logger.
     */
    private STLLogger logger;
    //============ Variables [END]

    @Override
    public void run() {
        if (serverSocket == null) return;
        try {
            Socket incoming_connection = serverSocket.accept();

            new STLThread(incoming_connection).run();

        } catch (IOException e) {
            logger.error(e);
        }
    }

    public static void main(String[] args) {


    }

}
