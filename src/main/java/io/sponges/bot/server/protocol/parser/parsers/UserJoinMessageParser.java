package io.sponges.bot.server.protocol.parser.parsers;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.channel.GroupChannel;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.api.entities.manager.UserManager;
import io.sponges.bot.api.event.events.user.UserJoinEvent;
import io.sponges.bot.api.event.framework.EventManager;
import io.sponges.bot.server.protocol.parser.framework.MessageParser;
import org.json.JSONObject;

public final class UserJoinMessageParser extends MessageParser {

    private final EventManager eventManager;

    public UserJoinMessageParser(EventManager eventManager) {
        super("USER_JOIN");
        this.eventManager = eventManager;
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
        manager.loadChannel(id, channel -> loadAdded(client, network, channel, content));
    }

    private void loadAdded(Client client, Network network, Channel channel, JSONObject content) {
        UserManager manager = network.getUserManager();
        String id = content.getString("added");
        manager.loadUser(id, added -> loadInitiator(client, network, channel, added, content));
    }

    private void loadInitiator(Client client, Network network, Channel channel, User added, JSONObject content) {
        UserManager manager = network.getUserManager();
        String id = content.getString("initiator");
        manager.loadUser(id, initiator -> handleEvent(client, network, channel, added, initiator));
    }

    private void handleEvent(Client client, Network network, Channel channel, User added, User initiator) {
        UserJoinEvent event = new UserJoinEvent(client, network, (GroupChannel) channel, added, initiator);
        eventManager.post(event);
    }
}
