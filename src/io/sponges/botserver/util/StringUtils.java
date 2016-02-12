package io.sponges.botserver.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class for String related things
 */
public class StringUtils {

    /**
     * Escapes html from a String
     * @param input String to escape html from
     * @return String with escaped html
     */
    public static String escape(String input) {
        String html3 = StringEscapeUtils.escapeHtml3(input);
        return StringEscapeUtils.escapeHtml4(html3);
    }

    /**
     * Formats the time period with a pre defined format
     * @param start the beginning of the time period
     * @param finish the end of the time period
     * @return formatted date String
     */
    public static String formatDate(long start, long finish) {
        return DurationFormatUtils.formatPeriod(start, finish, "d 'days,' H 'hours,' m 'mins and' s 'seconds'");
    }

    public static List<String> extractUrls(String text) {
        text = StringEscapeUtils.unescapeJson(text);
        List<String> containedUrls = new ArrayList<String>();
        //String urlRegex = "((https?):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        String urlRegex = "(?:(?:https?):\\/\\/|www\\.|ftp\\.)(?:\\([-A-Z0-9+&@#\\/%=~_|$?!:,.]*\\)|[-A-Z0-9+&@#\\/%=~_|$?!:,.])*(?:\\([-A-Z0-9+&@#\\/%=~_|$?!:,.]*\\)|[A-Z0-9+&@#\\/%=~_|$])";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
        }

        Msg.debug("URLS " + containedUrls);

        return containedUrls;
    }

    public static boolean isValidInput(String input) {
        String regex = "(^[a-zA-Z0-9]*$)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

}
