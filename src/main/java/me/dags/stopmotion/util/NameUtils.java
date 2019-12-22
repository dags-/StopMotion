package me.dags.stopmotion.util;

public class NameUtils {

    public static String sanitize(String name) {
        StringBuilder sb = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            char c = Character.toLowerCase(name.charAt(i));
            if (c == ' ') {
                c = '_';
            } else if (!Character.isAlphabetic(c) && !Character.isDigit(c) && c != '_') {
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
