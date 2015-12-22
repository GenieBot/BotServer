package pw.sponges.botserver.cmd.commands.mc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;
import pw.sponges.botserver.util.Msg;
import pw.sponges.botserver.util.Scheduler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MirrorCommand extends Command {

    private Map<MirrorFile, MirrorItem> cached;
    private String noArgs;

    public MirrorCommand() {
        super("command.mirror", UserRole.USER, "grabs the latest builds from tcpr.ca", "spigotbuilds", "tcpr", "spigot", "bukkit", "mirror", "yivemirror", "files");

        this.cached = new HashMap<>();

        Scheduler.runAsyncTaskRepeat(this::reload, 0, 6, TimeUnit.HOURS);

        StringBuilder str = new StringBuilder("Available files on the http://tcpr.ca mirror:\n");
        for (MirrorFile file : MirrorFile.values()) {
            str.append(file.name().toLowerCase().replace("_", "-")).append(", ");
        }
        str.deleteCharAt(str.length() - 2).append("\nUsage: tcpr [file]\nNote: the adf.ly link is provided by tcpr.ca");
        this.noArgs = str.toString();
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply(noArgs);
            return;
        }

        if (cached.size() == 0) {
            request.reply("Not loaded yet, please wait a few minutes!");
            return;
        }

        String file = args[0].toUpperCase().replace("-", "_");
        MirrorFile mirrorFile = MirrorFile.valueOf(file);

        if (mirrorFile == null) {
            request.reply("Invalid file '" + args[0] + "'!");
            return;
        }

        MirrorItem item = cached.get(mirrorFile);
        request.reply("Latest " + args[0] + " build:\nName: " + item.getName() + "\nDate: " + item.getDate() + "\nLink: " + item.getLink() + "\nSupplied by http://tcpr.ca");
    }

    private void reload() {
        Msg.debug("Reloading tcpr...");

        for (MirrorFile file : MirrorFile.values()) {
            try {
                MirrorItem item = scrape(file.getUrl());
                this.cached.put(file, item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Msg.debug("Reloaded tcpr!");
    }

    private MirrorItem scrape(String url) throws IOException {
        Document document = Jsoup.connect(url).ignoreContentType(true).get();
        Elements elements = document.getElementsByClass("table-list-search");
        Element element = elements.get(0);
        Element tbody = element.getElementsByTag("tbody").get(0);
        Element tr = tbody.getElementsByTag("tr").get(0);
        Elements elements1 = tr.getElementsByTag("td");
        String name = elements1.get(0).text();
        String date = elements1.get(1).text();
        String dl = elements1.get(2).getElementsByTag("a").get(0).attr("href");

        Msg.debug("Latest build for " + url + ":\n" + name + "\n" + date + "\n" + dl);
        return new MirrorItem(name, date, dl);
    }

} class MirrorItem {

    private final String name, date, link;

    MirrorItem(String name, String date, String link) {
        this.name = name;
        this.date = date;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {
        return link;
    }

} enum MirrorFile {

    BUKKIT("http://tcpr.ca/bukkit"),
    CRAFTBUKKIT("http://tcpr.ca/craftbukkit"),
    SPIGOT("http://tcpr.ca/spigot"),
    PAPERSPIGOT("http://tcpr.ca/paperspigot"),
    KCAULDRON("http://tcpr.ca/kcauldron"),
    KCAULDRON_BACKPORTS("http://tcpr.ca/kcauldron-backports"),
    CAULDRON("http://tcpr.ca/cauldron"),
    MCPC("http://tcpr.ca/mcpc");

    private final String url;

    MirrorFile(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
