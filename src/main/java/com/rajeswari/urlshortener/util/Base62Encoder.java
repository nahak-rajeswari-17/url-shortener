package com.rajeswari.urlshortener.util;

public class Base62Encoder {

    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(long value) {

        if (value < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }

        if (value == 0) {
            return "0";
        }

        StringBuilder shortCode = new StringBuilder();

        while (value > 0) {
            int remainder = (int) (value % 62);
            shortCode.append(BASE62.charAt(remainder));
            value = value / 62;
        }

        return shortCode.reverse().toString();
    }
}