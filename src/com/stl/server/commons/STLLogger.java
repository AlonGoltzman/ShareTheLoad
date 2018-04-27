package com.stl.server.commons;

import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Logs all data to execution path + server.log
 * TODO: Add thread independent support.
 * TODO: Add custom pattern support.
 */
public class STLLogger {

    //============ Singleton [START]

    public static STLLogger getInstance(){
        return instance == null? (instance = new STLLogger()) : instance;
    }

    private static STLLogger instance = new STLLogger();


    private STLLogger() {
        try {
            URL resourceURL = getClass().getClassLoader().getResource(".gubed");
            if (resourceURL == null)
                logLevel = Level.INFO;
            else {
                File debug_file = new File(resourceURL.getFile());
                String debugLiteral = new BufferedReader(new FileReader(debug_file)).readLine();
                logLevel = Level.getLevel(debugLiteral);
            }
        } catch (IOException e) {
            Logger logger = Logger.getLogger("Failure STLLogger");
            logger.throwing("STLLogger", "Constructor", e);
            logLevel = Level.INFO;
        }
    }

    //============ Singleton [END]

    //============ Variables [START]
    /**
     * The log output file.
     */
    private final File LOG_FILE = new File(System.getProperty("user.dir") + File.separator + "stl.log");

    /**
     * The log message date format.
     */
    private final DateFormat LOG_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

    /**
     * Has an error been encountered during the last print attempt.
     * If it has been then print won't try to perform it's function anymore.
     */
    private boolean err_during_print = false;

    /**
     * The current log level, indicating the level of output.
     */
    private static Level logLevel;

    //============ Variables [END]

    //============ Functions [START]

    /**
     * A log message indicating that it is entering a method.
     *
     * @param className
     *         the class that issued the message.
     * @param methodName
     *         the method name.
     */
    public void entering(String className, String methodName) {
        print(Level.INFO, String.format("Entering %s --> %s.", className, methodName));
    }

    /**
     * A log message indicating that it is exiting a method, meaning that the method has finished it's work.
     *
     * @param className
     *         the class that issued the message.
     * @param methodName
     *         the method name.
     */
    public void exiting(String className, String methodName) {
        print(Level.INFO, String.format("Exiting %s --> %s.", className, methodName));
    }

    /**
     * A log message that is only printed upon usage of debug level output.
     *
     * @param message
     *         the debug message.
     */
    public void debug(String message) {
        print(Level.DEBUG, message);
    }

    /**
     * A log message indicating that there is a warning that needs to be noticed (or not).
     *
     * @param className
     *         the class that issued the message.
     * @param methodName
     *         the method name.
     * @param warning
     *         the warning message.
     */
    public void warning(String className, String methodName, String warning) {
        print(Level.WARN, String.format("%s --> %s: %s", className, methodName, warning));
    }

    /**
     * A log message indicating that there is a warning that needs to be noticed (or not).
     *
     * @param className
     *         the class that issued the message.
     * @param methodName
     *         the method name.
     * @param warning
     *         the warning message, this type will provide a stacktrace.
     */
    public void warning(String className, String methodName, Throwable warning) {
        StringWriter error_str = new StringWriter();
        warning.printStackTrace(new PrintWriter(error_str));
        print(Level.WARN, String.format("%s --> %s:\n%s", className, methodName, error_str.toString()));
    }

    /**
     * A log message indicating that an error has occurred, usually these errors are not fatal to the program.
     *
     * @param error
     *         the error that occurred.
     */
    public void error(Throwable error) {
        StringWriter error_str = new StringWriter();
        error.printStackTrace(new PrintWriter(error_str));
        print(Level.WARN, error_str.toString());
    }

    /**
     * A log message indicating that an error has occurred, this is probably a program terminating issue.
     *
     * @param error
     *         the error that occurred.
     */
    public void fatal(Throwable error) {
        StringWriter error_str = new StringWriter();
        error.printStackTrace(new PrintWriter(error_str));
        print(Level.WARN, error_str.toString());
    }
    //============ Functions [END]

    //============ Root Funciton [START]

    private void print(Level minimum_level, String message) {
        if (err_during_print) return;
        if (!Level.isLevelPriorToLevel(minimum_level, logLevel)) return;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true));

            Date now = new Date();
            now.setTime(System.currentTimeMillis());

            String logLevelLiteral = logLevel.toString();

            StringBuilder builder = new StringBuilder();
            builder.append(LOG_DATE_FORMAT.format(now)).append(" ")
                    .append("[").append(logLevelLiteral).append("]");

            int missedSpaces = 32 - builder.length();
            for (int i = 0; i < missedSpaces; i++) builder.append(" ");

            builder.append(": ").append(message);

            writer.write(builder.toString());
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Logger logger = Logger.getLogger("Failure STLLogger");
            logger.throwing("STLLogger", "print", e);
            err_during_print = true;
        }

    }

    //============ Root Function [END]

    //============ Level Definition [START]

    private enum Level {
        OFF(0),
        FATAL(1),
        ERROR(2),
        WARN(3),
        INFO(4),
        DEBUG(5);

        private int level;

        Level(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public static Level getLevel(String level) {
            switch (level) {
                case "0":
                    return OFF;
                case "1":
                    return FATAL;
                case "2":
                    return ERROR;
                case "3":
                    return WARN;
                case "4":
                    return INFO;
                case "5":
                    return DEBUG;
                default:
                    return INFO;
            }
        }

        public static boolean isLevelPriorToLevel(Level minimumLevel, Level givenLevel) {
            return minimumLevel.level < givenLevel.level;
        }
    }

    //============ Level Definition [END]

}
