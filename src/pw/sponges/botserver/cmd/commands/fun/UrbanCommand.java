package pw.sponges.botserver.cmd.commands.fun;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;
import pw.sponges.botserver.util.Msg;

import java.io.IOException;
import java.net.URLEncoder;

public class UrbanCommand extends Command {

    private final String URL = "http://www.urbandictionary.com/define.php?term=";

    public UrbanCommand() {
        super("command.urban", UserRole.USER, "looks up a term on urban dictionary", "urban", "urbandictionary", "udictionary");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("U st00pid! Usage: urban <term>");
            return;
        }

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            str.append(URLEncoder.encode(args[i]));

            if (i != args.length - 1) {
                str.append("%20");
            }
        }

        String query = str.toString();
        Msg.debug("[Urban command] Query=" + query);

        JSONObject json = null;
        try {
            json = searchUD(query, request.getUser().getId());
        } catch (IOException e) {
            e.printStackTrace();
            request.reply("Something went wrong whilst querying urbandictionary :(");
            return;
        }

        if (json.getString("result_type").equals("no_results")) {
            request.reply("No results ;o");
            return;
        }

        JSONArray array = json.getJSONArray("list");
        JSONObject object = array.getJSONObject(0);
        Msg.debug("[Urban command] " + object.toString());

        String definition = StringUtils.abbreviate(object.getString("definition"), 300);
        String link = object.getString("permalink");
        int up = object.getInt("thumbs_up");
        int down = object.getInt("thumbs_down");

        StringBuilder response = new StringBuilder();
        response.append(definition).append(" (+").append(up).append(" / -").append(down).append(")\nPermalink: ").append(link);

        request.reply(response.toString());
    }

    private JSONObject searchUD(String query, String user) throws IOException, JSONException {
        String document = Jsoup.connect("http://api.urbandictionary.com/v0/define?term=" + query).ignoreContentType(true).header("X-Request-By-Username", user).execute().body();
        Msg.debug("[Urban command] " + document);
        return new JSONObject(document);
    }

    /*@Override
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

            if (definitions.size() > 0) {
                String definition = definitions.get(0).getElementsByClass("meaning").get(0).text();
                if (definition.length() > 300) {
                    definition = definition.substring(0, 300) + "...";
                }
                str.append(definition).append("\nMore definitions @ ").append(builder.toString());
            }

            request.reply(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
            request.reply("Something got kill :s");
        }
    }*/

}
