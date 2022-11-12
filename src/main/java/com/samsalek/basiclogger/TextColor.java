package com.samsalek.basiclogger;

enum TextColor {
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

   TextColor(String value) {
        this.value = value;
    }
}
