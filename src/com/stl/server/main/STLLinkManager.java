package com.stl.server.main;

import javafx.util.Pair;

import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class STLLinkManager {

    //============ Singleton [START]

    private final static Object lock = new Object();

    private final static STLLinkManager instance = new STLLinkManager();

    public synchronized static STLLinkManager getInstance() {
        synchronized (lock) {
            return instance;
        }
    }

    private STLLinkManager() {
        connection_table = new ConcurrentHashMap<>();
    }

    //============ Singleton [END]


    //============ Variables [START]

    /**
     * A map correlating between a unique identifier and a socket pair.
     * The socket pair are host, client.
     */
    private static ConcurrentHashMap<String, Pair<Socket, Socket>> connection_table = null;

    //============ Variables [END]

    //============ Methods [END]

    public void introduceHost(Socket host) {
        Collection<Pair<Socket, Socket>> allLinks = connection_table.values();
        boolean hostExists = allLinks.stream().anyMatch(socketSocketPair -> socketSocketPair.getKey().getInetAddress().equals(host.getInetAddress()));
        if(hostExists){}


    }
}
