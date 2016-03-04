package io.sponges.bot.server.cmd.framework;

import io.sponges.bot.server.Client;
import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.framework.User;
import io.sponges.bot.server.util.Advertisements;
import org.apache.commons.lang3.StringEscapeUtils;
import io.sponges.bot.server.framework.Network;
import io.sponges.bot.server.messages.CmdResponseMessage;

import java.util.List;
import java.util.Random;

public class CommandRequest {

    private final User user;
    private final Room room;
    private final Network network;
    private final Client client;
    private final String input;

    private final Advertisements advertisements;

    public CommandRequest(User user, String input) {
        this.user = user;
        this.room = user.getRoom();
        this.network = room.getNetwork();
        this.client = network.getClient();
        this.input = input;

        this.advertisements = Advertisements.getInstance();
    }

    public User getUser() {
        return user;
    }

    public Room getRoom() {
        return room;
    }

    public Network getNetwork() {
        return network;
    }

    public Client getClient() {
        return client;
    }

    public String getInput() {
        return input;
    }

    public void reply(String message) {
        client.sendMessage(new CmdResponseMessage(user, insertAd(message)));
    }

    // TODO much better ad inserting system
    private String insertAd(String message) {
        Random random = new Random();
        int rand = random.nextInt(5);

        if (rand == 0) {
            List<String> ads = advertisements.getAdvertisements();
            String msg = message + "\nAd: " + ads.get(random.nextInt(ads.size()));
            return StringEscapeUtils.unescapeJson(msg);
        }

        return message;
    }
}