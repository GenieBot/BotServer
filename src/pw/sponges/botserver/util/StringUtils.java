package pw.sponges.botserver.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class StringUtils {

    public static String escape(String input) {
        String html3 = StringEscapeUtils.escapeHtml3(input);
        return StringEscapeUtils.escapeHtml4(html3);
    }

    public static String formatDate(long start, long finish) {
        return DurationFormatUtils.formatPeriod(start, finish, "d 'days,' H 'hours,' m 'mins and' s 'seconds'");
    }

}
