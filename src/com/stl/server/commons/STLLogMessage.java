package com.stl.server.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

/**
 * A log message from STLLogger to STLCentralLogger.
 */
public class STLLogMessage {

    /**
     * The timestamp of the message creation.
     */
    private Long timestamp;

    /**
     * The thread id of the thread that the message is sent from.
     */
    private Long threadID;

    /**
     * The message itself.
     */
    private String message;

    /**
     * The log level.
     */
    private STLLogLevel logLevel;

    /**
     * The log message date format.
     */
    private transient final DateFormat LOG_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

    public STLLogMessage(Long timestamp, Long threadID, String message, STLLogLevel logLevel) {
        this.timestamp = timestamp;
        this.threadID = threadID;
        this.message = message;
        this.logLevel = logLevel;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getThreadID() {
        return threadID;
    }

    public String getMessage() {
        return message;
    }

    public STLLogLevel getLogLevel() {
        return logLevel;
    }

    /*
     * The reason that this is false is due to the fact that two log messages are never identi
     */
    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public String toString() {
        Date now = new Date();
        now.setTime(getTimestamp());

        String logLevelLiteral = getLogLevel().toString();

        StringBuilder builder = new StringBuilder();
        builder.append(LOG_DATE_FORMAT.format(now)).append(" ")
                .append("[").append(logLevelLiteral).append("]");

        if (builder.length() != 31)
            builder.append(" ");

        builder.append("[TID=").append(getThreadID()).append("]");

        int extraSpaces = 6;

        Long threadID_clone = getThreadID();
        while (threadID_clone > 0) {
            threadID_clone /= 10L;
            extraSpaces++;
        }

        int missedSpaces = (32 + extraSpaces) - builder.length();
        for (int i = 0; i < missedSpaces; i++) builder.append(" ");

        builder.append(": ").append(message);
        return builder.toString();
    }
}
