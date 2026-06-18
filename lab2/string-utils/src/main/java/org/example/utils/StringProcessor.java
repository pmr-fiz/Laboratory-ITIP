package org.example.utils;
import org.apache.commons.lang3.StringUtils;

public class StringProcessor {
    public static String processString(String input) {
        if (input == null) return "";
        return StringUtils.reverse(input);
    }
}
