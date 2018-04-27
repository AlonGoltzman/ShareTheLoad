package com.stl.plugin.main;

import com.intellij.openapi.diagnostic.Logger;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;

import static com.stl.plugin.main.Host.INCOMING_PASSWORD_HASH;

public class Client {


    private Socket socket;

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unchecked"})
    public Client(String ip, String password) {
        try {
            socket = new Socket(ip, 31274);
            JSONObject object = new JSONObject();
            object.put(INCOMING_PASSWORD_HASH, password);
            String passJSON = object.toJSONString();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.write(passJSON);
            writer.flush();

            if ("ok".equals(reader.readLine()))
                new Thread(this::listen).run();
            else
                Logger.getInstance(Client.class).warn("Error during connection creation, please retry.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {

    }
}
