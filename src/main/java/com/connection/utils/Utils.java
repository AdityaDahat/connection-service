package com.connection.utils;

public class Utils {
    private Utils() {

    }

    public static String getBeanName(String className) {
        char first = className.charAt(0);
        return className.replaceFirst(String.valueOf(first), String.valueOf(first).toLowerCase());
    }
}
