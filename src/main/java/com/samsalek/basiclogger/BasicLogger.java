package com.samsalek.basiclogger;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BasicLogger {
    private static final Map<String, BasicLogger> LOGGER_MAP = new HashMap<>();
    public static final String MAIN_LOGGER = "MAIN";

    private int row = 0;
    private String savePath;

    private final int bufferSize = 1000;
    private final String[] buffer = new String[bufferSize];;
    private int bufferPos = 0;

    private BasicLogger() {}

    public static BasicLogger get() {
        return get(MAIN_LOGGER);
    }

    public static BasicLogger get(String loggerName) {
        if(LOGGER_MAP.get(loggerName.toUpperCase()) == null) {
            LOGGER_MAP.put(loggerName, new BasicLogger());
        }

        return LOGGER_MAP.get(loggerName);
    }

    public void setLogSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void log(String text, TextColor color) {
        print(text, color);
    }

    public void info(String text) {
        print("INFO - " + text, TextColor.CYAN);
    }

    public void warning(String text) {
        print("WARNING - " + text, TextColor.YELLOW);
    }

    public void error(String text) {
        print("ERROR - " + text, TextColor.RED_BOLD);
    }

    private void print(String text, TextColor color) {
        if(row == 0) {
            System.out.println();
        }
        System.out.println(row + " : " + color.value + text + TextColor.RESET.value);
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

    public void createLogFile() {
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
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
