package io.sponges.bot.server.util;

public final class ValidationUtils {

    public static boolean isValidJson(String input) {
        return input.startsWith("{") && input.endsWith("}");
    }

}
