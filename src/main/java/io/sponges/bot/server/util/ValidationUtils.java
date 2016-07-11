package io.sponges.bot.server.util;

public final class ValidationUtils {

    public static boolean isValidJson(String input) {
        return input != null && input.charAt(0) =='{' && input.charAt(input.length() - 1) == '}';
    }

}
