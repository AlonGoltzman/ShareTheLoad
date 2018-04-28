package com.stl.server.commons;

import java.io.*;

/**
 * Logs all data to execution path + server.log
 * TODO: Add thread independent support.
 * TODO: Add custom pattern support.
 */
public class STLLogger {

    //============ Variables [START]


    //============ Variables [END]

    //============ Functions [START]

    /**
     * A log message indicating that it is entering a method.
     *
     * @param fromClass
     *         the class that issued the message.
     * @param fromMethod
     *         the method name.
     */
    public void entering(String fromClass, String fromMethod) {
        print(STLLogLevel.INFO, String.format("Entering %s --> %s.", fromClass, fromMethod));
    }

    /**
     * A log message indicating that it is exiting a method, meaning that the method has finished it's work.
     *
     * @param fromClass
     *         the class that issued the message.
     * @param fromMethod
     *         the method name.
     */
    public void exiting(String fromClass, String fromMethod) {
        print(STLLogLevel.INFO, String.format("Exiting %s --> %s.", fromClass, fromMethod));
    }

    /**
     * A log message that is only printed upon usage of debug level output.
     *
     * @param message
     *         the debug message.
     */
    public void debug(String message) {
        print(STLLogLevel.DEBUG, message);
    }

    /**
     * A log message that is only printed upon usage of debug level output.
     *
     * @param fromClass
     *         the class that issued the message.
     * @param fromMethod
     *         the method name.
     * @param message
     *         the debug message.
     */
    public void debug(String fromClass, String fromMethod, String message) {
        print(STLLogLevel.DEBUG, String.format("%s --> %s : %s", fromClass, fromClass, message));
    }

    /**
     * A log message indicating that there is a warning that needs to be noticed (or not).
     *
     * @param message
     *         the warning message.
     */
    public void warning(String message) {
        print(STLLogLevel.WARN, message);

    }

    /**
     * A log message indicating that there is a warning that needs to be noticed (or not).
     *
     * @param error
     *         the warning message, this type will provide a stacktrace.
     */
    public void warning(Throwable error) {
        StringWriter error_str = new StringWriter();
        error.printStackTrace(new PrintWriter(error_str));
        print(STLLogLevel.WARN, error_str.toString());
    }

    /**
     * A log message indicating that there is a warning that needs to be noticed (or not).
     *
     * @param fromClass
     *         the class that issued the message.
     * @param fromMethod
     *         the method name.
     * @param message
     *         the warning message.
     */
    public void warning(String fromClass, String fromMethod, String message) {
        print(STLLogLevel.WARN, String.format("%s --> %s: %s", fromClass, fromMethod, message));
    }

    /**
     * A log message indicating that there is a warning that needs to be noticed (or not).
     *
     * @param fromClass
     *         the class that issued the message.
     * @param fromMethod
     *         the method name.
     * @param error
     *         the warning message, this type will provide a stacktrace.
     */
    public void warning(String fromClass, String fromMethod, Throwable error) {
        StringWriter error_str = new StringWriter();
        error.printStackTrace(new PrintWriter(error_str));
        print(STLLogLevel.WARN, String.format("%s --> %s:\n%s", fromClass, fromMethod, error_str.toString()));
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
        print(STLLogLevel.ERROR, error_str.toString());
    }

    /**
     * A log message indicating that an error has occurred, usually these errors are not fatal to the program.
     *
     * @param fromClass
     *         the class that issued the message.
     * @param fromMethod
     *         the method name.
     * @param error
     *         the error that occurred.
     */
    public void error(String fromClass, String fromMethod, Throwable error) {
        StringWriter error_str = new StringWriter();
        error.printStackTrace(new PrintWriter(error_str));
        print(STLLogLevel.ERROR, String.format("%s --> %s:\n%s", fromClass, fromMethod, error_str.toString()));
    }

    /**
     * A log message indicating that an error has occurred, usually these errors are not fatal to the program.
     *
     * @param fromClass
     *         the class that issued the message.
     * @param fromMethod
     *         the method name.
     * @param message
     *         A message to accompany the error.
     * @param error
     *         the error that occurred.
     */
    public void error(String fromClass, String fromMethod, String message, Throwable error) {
        StringWriter error_str = new StringWriter();
        error.printStackTrace(new PrintWriter(error_str));
        print(STLLogLevel.ERROR, String.format("%s --> %s : %s\n%s", fromClass, fromMethod, message, error_str.toString()));
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
        print(STLLogLevel.FATAL, error_str.toString());
    }

    /**
     * A log message indicating that an error has occurred, this is probably a program terminating issue.
     *
     * @param fromClass
     *         the class that issued the message.
     * @param fromMethod
     *         the method name.
     * @param error
     *         the error that occurred.
     */
    public void fatal(String fromClass, String fromMethod, Throwable error) {
        StringWriter error_str = new StringWriter();
        error.printStackTrace(new PrintWriter(error_str));
        print(STLLogLevel.FATAL, String.format("%s --> %s:\n%s", fromClass, fromMethod, error_str.toString()));
    }

    /**
     * A log message indicating that an error has occurred, this is probably a program terminating issue.
     *
     * @param fromClass
     *         the class that issued the message.
     * @param fromMethod
     *         the method name.
     * @param message
     *         A message to accompany the error.
     * @param error
     *         the error that occurred.
     */
    public void fatal(String fromClass, String fromMethod, String message, Throwable error) {
        StringWriter error_str = new StringWriter();
        error.printStackTrace(new PrintWriter(error_str));
        print(STLLogLevel.FATAL, String.format("%s --> %s : %s\n%s", fromClass, fromMethod, message, error_str.toString()));
    }

    //============ Functions [END]

    //============ Root Funciton [START]

    private void print(STLLogLevel minimum_level, String message) {
        if (!STLLogLevel.isLevelPriorToLevel(minimum_level, STLCentralLogger.logLevel)) return;

        STLLogMessage log_message = new STLLogMessage(System.currentTimeMillis(), Thread.currentThread().getId(), message, minimum_level);
        STLCentralLogger.getLogger().addMessage(log_message);
    }

    //============ Root Function [END]

}
