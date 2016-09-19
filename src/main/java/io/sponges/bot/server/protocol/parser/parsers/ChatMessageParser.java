package io.sponges.bot.server.protocol.parser.parsers;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.api.entities.manager.UserManager;
import io.sponges.bot.api.entities.message.ReceivedMessage;
import io.sponges.bot.api.event.events.message.MessageReceivedEvent;
import io.sponges.bot.server.entities.message.ReceivedMessageImpl;
import io.sponges.bot.server.event.framework.EventBus;
import io.sponges.bot.server.protocol.parser.framework.MessageParser;
import org.json.JSONObject;

import java.util.Date;

public final class ChatMessageParser extends MessageParser {

    private final EventBus eventBus;

    public ChatMessageParser(EventBus eventBus) {
        super("CHAT");
        this.eventBus = eventBus;
    }

    @Override
    public void parse(Client client, long time, JSONObject content) {
        NetworkManager manager = client.getNetworkManager();
        String id = content.getString("network");
        manager.loadNetwork(id, network -> loadChannel(client, network, content));
    }

    private void loadChannel(Client client, Network network, JSONObject content) {
        ChannelManager manager = network.getChannelManager();
        String id = content.getString("channel");
        manager.loadChannel(id, channel -> loadUser(client, network, channel, content));
    }

    private void loadUser(Client client, Network network, Channel channel, JSONObject content) {
        UserManager manager = network.getUserManager();
        String id = content.getString("user");
        manager.loadUser(id, user -> handleMessage(client, network, channel, user, content));
    }

    private void handleMessage(Client client, Network network, Channel channel, User user, JSONObject content) {
        JSONObject json = content.getJSONObject("message");
        String text = json.getString("content");
        if (text.length() == 0) return;
        long time = json.getLong("time");
        Date date = new Date(time);
        ReceivedMessage message = new ReceivedMessageImpl(client, network, channel, user, date, text, null);
        MessageReceivedEvent event = new MessageReceivedEvent(client, network, channel, user, message);
        eventBus.post(event);
    }
}
