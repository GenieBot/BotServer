package pw.sponges.botserver.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

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

}
