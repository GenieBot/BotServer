package io.sponges.botserver.util;

public class ParsingUtils {

    public static Object parse(String input) {
        // integer
        try { return Integer.parseInt(input);
        } catch (NumberFormatException ignored) {}

        // double
        try { return Double.parseDouble(input);
        } catch (NumberFormatException ignored) {}

        // boolean
        String lower = input.toLowerCase();
        if (lower.equals("true") || lower.equals("false")) {
            return Boolean.parseBoolean(lower);
        }

        // string
        return input;
    }

}
