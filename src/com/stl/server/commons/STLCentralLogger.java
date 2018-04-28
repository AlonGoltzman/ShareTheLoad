package com.stl.server.commons;

import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * The central logger is responsible for the actual logging process.
 */
public class STLCentralLogger extends Thread {

    //============ Singleton [START]

    /**
     * An ordered set of all messages that need to be printed at the moment.
     */
    private static final List<STLLogMessage> messages_list = Collections.synchronizedList(new ArrayList<>());

    public static STLCentralLogger getLogger() {
        return instance;
    }

    private static STLCentralLogger instance = new STLCentralLogger();

    private STLCentralLogger() {
        try {
            URL resourceURL = getClass().getClassLoader().getResource(".gubed");
            if (resourceURL == null)
                logLevel = STLLogLevel.INFO;
            else {
                File debug_file = new File(resourceURL.getFile());
                String debugLiteral = new BufferedReader(new FileReader(debug_file)).readLine();
                logLevel = STLLogLevel.getLevel(debugLiteral);
            }
        } catch (IOException e) {
            StringWriter errorWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(errorWriter));
            System.out.println(errorWriter.toString());

            Logger logger = Logger.getLogger("Failure STLLogger");
            logger.throwing("STLLogger", "Constructor", e);
            logLevel = STLLogLevel.INFO;
        } finally {
            System.out.println(LOG_FILE.toString());
        }

        try {
            if (!LOG_FILE.exists()) {
                boolean ignored = LOG_FILE.getParentFile().mkdirs();
                if (!LOG_FILE.createNewFile()) {
                    err_during_print = true;
                    System.out.println("Failed to create new file.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        start();
    }

    //============ Singleton [END]

    //============ Variables [START]


    /**
     * The log output file.
     */
    private final File LOG_FILE = new File(System.getProperty("user.dir") + File.separator + "stl.log");


    /**
     * Has an error been encountered during the last print attempt.
     * If it has been then print won't try to perform it's function anymore.
     */
    private boolean err_during_print = false;

    /**
     * A flag to indicate if a log file release was requested or not.
     */
    private boolean log_switch_requested = false;

    /**
     * The current log level, indicating the level of output.
     */
    public static STLLogLevel logLevel;

    private final Object lock = new Object();

    //============ Variables [END]

    //============ Methods [START]

//    private void print(STLLogLevel minimum_level, String message) {
//
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true));
//
//            Date now = new Date();
//            now.setTime(System.currentTimeMillis());
//
//            String logLevelLiteral = logLevel.toString();
//
//            StringBuilder builder = new StringBuilder();
//            builder.append(LOG_DATE_FORMAT.format(now)).append(" ")
//                    .append("[").append(logLevelLiteral).append("]");
//
//            int missedSpaces = 32 - builder.length();
//            for (int i = 0; i < missedSpaces; i++) builder.append(" ");
//
//            builder.append(": ").append(message);
//
//            writer.write(builder.toString());
//            writer.newLine();
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            Logger logger = Logger.getLogger("Failure STLLogger");
//            logger.throwing("STLLogger", "print", e);
//            err_during_print = true;
//        }
//    }

    void addMessage(STLLogMessage message) {
        synchronized (lock) {
            System.out.println("Message added");
            messages_list.add(message);
        }
    }
    //============ Method [END]

    //============ Root Method [START]

    @Override
    public void run() {
        while (true) {

            //SortedMap<Long, Integer> timestamp_to_thread_id_clone = new TreeMap<>(timestamp_to_thread_id_map);

            //Clone content of the messages file.
            List<STLLogMessage> messages_clone;
            synchronized (lock) {
                messages_clone = new ArrayList<>(messages_list);
                messages_list.clear();
            }

            messages_clone.sort(Comparator.comparing(STLLogMessage::getTimestamp));
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true));
                for (STLLogMessage message : messages_clone) {
                    if (err_during_print) return;

                    //if (!STLLogLevel.isLevelPriorToLevel(message.getLogLevel(), logLevel)) return;

                    writer.write(message.toString());
                    writer.newLine();
                }

                writer.flush();
                writer.close();
            } catch (IOException e) {
                Logger logger = Logger.getLogger("Failure STLLogger");
                logger.throwing("STLLogger", "print", e);
                err_during_print = true;
            } finally {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //============ Root Method [END]
}
