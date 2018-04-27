package com.stl.plugin.main;

import com.intellij.openapi.diagnostic.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static com.stl.plugin.main.ShareTheLoad.PASSWORD_HASH;
import static com.stl.plugin.main.ShareTheLoad.config_fetch;

public class Host {

    private static Host instance;

    public static final String INCOMING_PASSWORD_HASH = "pass";

    private boolean running = false;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private boolean established = false;

    public static Host getInstance() throws IOException {
        return instance == null ? (instance = new Host()) : instance;
    }

    private ServerSocket socket;

    private Host() throws IOException {
        instance = this;
        socket = new ServerSocket(31274);

        Socket inbound = socket.accept();
        reader = new BufferedReader(new InputStreamReader(inbound.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(inbound.getOutputStream()));

        running = true;
        new Thread(this::listen).run();

    }

    private void listen() {
        try {
            while (running) {
                if (!established) {
                    JSONObject object = readObject();
                    String hash = object.get(INCOMING_PASSWORD_HASH).toString();
                    if (!hash.equals(config_fetch(PASSWORD_HASH).toString())) {
                        socket.close();
                        instance = null;
                        return;
                    }
                    established = true;
                    writer.write("ok");
                    writer.flush();
                    Logger.getInstance(Host.class).warn("Connection failure, please enable 'Externalize Workspace'");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject readObject() throws IOException {
        JSONObject object = null;
        String line = null;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            try {
                object = (JSONObject) new JSONParser().parse(builder.toString());
                break;
            } catch (ParseException ignored) {
            }
        }
        return object;
    }
}
