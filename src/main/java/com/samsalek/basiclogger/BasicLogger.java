package com.samsalek.activityjournal.services.logger;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Logger {

    private static final Map<String, Logger> LOGGER_MAP = new HashMap<>();
    public static final String MAIN_LOGGER = "MAIN";

    private int row = 0;
    private String savePath;

    private final int bufferSize = 1000;
    private final String[] buffer = new String[bufferSize];;
    private int bufferPos = 0;

    private Logger() {}

    public static Logger get() {
        return get(MAIN_LOGGER);
    }

    public static Logger get(String loggerName) {
        if(LOGGER_MAP.get(loggerName) == null) {
            LOGGER_MAP.put(loggerName, new Logger());
        }

        return LOGGER_MAP.get(loggerName);
    }

    public void setLogSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void log(String text, Color color) {
        print(text, color);
    }

    public void info(String text) {
        print("INFO - " + text, Color.CYAN);
    }

    public void warning(String text) {
        print("WARNING - " + text, Color.YELLOW);
    }

    public void error(String text) {
        print("ERROR - " + text, Color.RED_BOLD);
    }

    private void print(String text, Color color) {
        if(row == 0) {
            System.out.println();
        }
        System.out.println(row + " : " + color.value + text + Color.RESET.value);
        row++;

        addToBuffer(text);
    }

    private void addToBuffer(String text) {
        if (bufferPos == bufferSize) {
            Collections.rotate(Arrays.asList(buffer), -1);
            bufferPos--;
        }

        buffer[bufferPos] = text;
        bufferPos++;
    }

    public void createLogFile(String stackTrace) {
        if(savePath == null) {
            throw new NullPointerException("Log save path is not set! Assign it with the method 'setLogSavePath'.");
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH'h'mm'm'ss's'");
        LocalDateTime now = LocalDateTime.now();

        try {
            String fileName = "log_" + dtf.format(now) + ".txt";
            FileWriter writer = new FileWriter(savePath + File.separatorChar + fileName);

            for (String s : buffer) {
                if(s != null) {
                    writer.write(s + "\n");
                }
            }
            writer.write(stackTrace);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Color {
        RESET("\u001b[0m"),
        BLACK("\u001b[30m"),
        WHITE("\u001b[37m"),
        RED("\u001b[31m"),
        RED_BOLD("\033[1;31m"),
        GREEN("\u001b[32m"),
        GREEN_BOLD("\033[1;32m"),
        BLUE("\u001b[34m"),
        YELLOW   ("\u001b[33m"),
        MAGENTA("\u001b[35m"),
        CYAN("\u001b[36m");

        final String value;

        Color(String value) {
            this.value = value;
        }
    }
}
