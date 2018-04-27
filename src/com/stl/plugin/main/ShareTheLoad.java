package com.stl.plugin.main;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public final class ShareTheLoad implements ApplicationComponent {

    public static final Logger logger = Logger.getInstance(ShareTheLoad.class);

    public static final String DEFAULT_SECTION = "share";
    public static final String ENABLED = "enabled";
    public static final String IPADDR = "ipaddr";
    public static final String PASSWORD_HASH = "passh";
    private static HashMap<String, Object> config = new HashMap<>();

    @Override
    public void initComponent() {
        init();
    }

    @SuppressWarnings("unchecked")
    private static void init() {
        try {
            Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
            File cnfg = new File(path.toFile().getParentFile(), "stl.cnfg");
            if (cnfg.exists()) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(cnfg));
                config = (HashMap<String, Object>) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void config_define(String key, Object value) {
        config.put(key, value);
        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
        File cnfg = new File(path.toFile().getParentFile(), "stl.cnfg");
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(cnfg));
            output.writeObject(config);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static Object config_fetch(String key) {
        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
        File cnfg = new File(path.toFile().getParentFile(), "stl.cnfg");
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(cnfg));
            config = (HashMap<String, Object>) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return config.getOrDefault(key, null);
    }

    public static String hashpw(@NotNull String pass) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(pass.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            String pass_hash = sb.toString();
            config_define(PASSWORD_HASH, pass_hash);
            return pass_hash;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
