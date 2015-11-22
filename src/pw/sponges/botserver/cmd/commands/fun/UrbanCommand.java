package pw.sponges.botserver.cmd.commands.fun;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;

import java.net.URLEncoder;

public class UrbanCommand extends Command {

    private final String URL = "http://www.urbandictionary.com/define.php?term=";

    public UrbanCommand() {
        super("command.urban", "urban", "urbandictionary", "udictionary");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("U st00pid! Usage: urban <term>");
            return;
        }

        StringBuilder str = new StringBuilder();

        StringBuilder builder = new StringBuilder(URL);
        StringBuilder query = new StringBuilder();
        for (String s : args) {
            builder.append(URLEncoder.encode(s)).append("+");
            query.append(s).append(" ");
        }
        query.setLength(query.length() - 1);
        builder.setLength(builder.length() - 1);

        try {
            Document d = Jsoup.connect(builder.toString()).header("X-Request-By-Username", request.getUser()).get();
            Elements definitions = d.getElementsByClass("def-panel");

            /*str.append("Your query for " + query.toString() + " returned " + definitions.size() + " result");
            if (definitions.size() != 1) {
                str.append("s");
            }
            str.append(".");*/
            if (definitions.size() > 0) {
                //str.append("\n");
                String definition = definitions.get(0).getElementsByClass("meaning").get(0).text();
                if (definition.length() > 300) {
                    definition = definition.substring(0, 300) + "...";
                }
                str.append("" + definition + "");
                str.append("\nMore definitions @ " + builder.toString());
            }

            request.reply(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
            request.reply("Something got kill :s");
        }
    }

}
