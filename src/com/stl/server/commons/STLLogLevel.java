package com.stl.server.commons;

enum STLLogLevel {
    OFF(0),
    FATAL(1),
    ERROR(2),
    WARN(3),
    INFO(4),
    DEBUG(5);

    private int level;

    STLLogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static STLLogLevel getLevel(String level) {
        switch (level.trim()) {
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

    public static boolean isLevelPriorToLevel(STLLogLevel minimumLevel, STLLogLevel givenLevel) {
        return minimumLevel.level <= givenLevel.level;
    }
}

