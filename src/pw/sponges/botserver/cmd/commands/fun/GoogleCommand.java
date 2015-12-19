package pw.sponges.botserver.cmd.commands.fun;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;

import java.net.URLEncoder;

public class GoogleCommand extends Command {

    public GoogleCommand() {
        super("command.google", UserRole.USER, "looks up a term on google", "google", "bing", "search");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("*sigh* usage: google [term]");
            return;
        }

        StringBuilder query = new StringBuilder();
        StringBuilder raw = new StringBuilder();
        for (String s : args) {
            query.append(URLEncoder.encode(s)).append("+");
            raw.append(s).append(" ");
        }

        query.setLength(query.length() - 1);
        raw.setLength(raw.length() - 1);
        String url = String.format("http://www.google.com/search?q=%s", query.toString());

        StringBuilder response = new StringBuilder();

        try {
            Document doc = Jsoup.connect(url).followRedirects(true).header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.0 Safari/537.36 SpongyBot/1.0").header("X-Request-By-Username", request.getUser()).get();
            Elements elements = doc.getElementsByClass("srg");
            if (elements.size() > 0) {
                Elements gs = elements.get(0).getElementsByClass("g");
                if (gs.size() > 0) {
                    Element first = gs.get(0);
                    Element a = first.getElementsByClass("r").get(0).getElementsByTag("a").get(0);
                    Element st = first.getElementsByClass("st").get(0);
                    response.append(a.attr("href")).append("\n");
                    String html = st.html();
                    html = Jsoup.clean(html, Whitelist.none());
                    response.append(StringEscapeUtils.unescapeHtml4(html));
                    response.append("\nMore results: ").append(url);
                } else {
                    response.append("Your query for <i>").append(raw.toString()).append("</i> returned <b>").append(gs.size()).append("</b> results");
                }
            } else {
                response.append("Your query for <i>").append(raw.toString()).append("</i> returned <b>0</b> results");
            }
        } catch (Exception e) {
            response.append("An exception occured while fetching google");
        }

        request.reply(response.toString());
    }
}
