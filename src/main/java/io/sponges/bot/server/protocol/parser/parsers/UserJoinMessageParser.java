package io.sponges.bot.server.protocol.parser.parsers;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.channel.GroupChannel;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.api.event.events.user.UserJoinEvent;
import io.sponges.bot.api.event.framework.Event;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.entities.NetworkImpl;
import io.sponges.bot.server.entities.UserImpl;
import io.sponges.bot.server.entities.manager.UserManagerImpl;
import io.sponges.bot.server.protocol.parser.framework.MessageParser;
import io.sponges.bot.server.protocol.parser.initalizer.ChannelInitializer;
import io.sponges.bot.server.protocol.parser.initalizer.NetworkInitializer;
import io.sponges.bot.server.protocol.parser.initalizer.UserInitializer;
import org.json.JSONObject;

public final class UserJoinMessageParser extends MessageParser {

    private final Bot bot;
    private final Storage storage;

    public UserJoinMessageParser(Bot bot) {
        super("USER_JOIN");
        this.bot = bot;
        this.storage = bot.getStorage();
    }

    @Override
    public void parse(Client client, long time, String messageId, JSONObject content) {
        NetworkImpl network;
        {
            String id = content.getString("network");
            NetworkManager manager = client.getNetworkManager();
            if (manager.isNetwork(id)) {
                network = (NetworkImpl) manager.getNetwork(id);
            } else {
                network = (NetworkImpl) NetworkInitializer.createNetwork(storage, client, id);
                manager.getNetworks().put(id, network);
            }
        }
        UserManagerImpl userManager = (UserManagerImpl) network.getUserManager();

        GroupChannel channel = null;
        if (!content.isNull("channel")) {
            JSONObject json = content.getJSONObject("channel");
            String id = json.getString("id");
            ChannelManager manager = network.getChannelManager();
            if (manager.isChannel(id)) {
                channel = (GroupChannel) manager.getChannel(id);
            } else {
                channel = (GroupChannel) ChannelInitializer.createChannel(storage, network, json);
                manager.getChannels().put(id, channel);
            }
        }

        UserImpl user;
        UserImpl initiator = null;
        {
            {
                JSONObject json = content.getJSONObject("added");
                String userId = json.getString("id");
                if (userManager.isUser(userId)) {
                    user = (UserImpl) userManager.getUser(userId);
                } else {
                    user = (UserImpl) UserInitializer.createUser(storage, network, json);
                    userManager.addUser(user);
                }
                if (channel != null && !channel.isUser(userId)) {
                    channel.getUsers().put(userId, user);
                }
            }
            if (!content.isNull("initiator")) {
                JSONObject json = content.getJSONObject("initiator");
                String userId = json.getString("id");
                if (userManager.isUser(userId)) {
                    initiator = (UserImpl) userManager.getUser(userId);
                } else {
                    initiator = (UserImpl) UserInitializer.createUser(storage, network, json);
                    userManager.addUser(initiator);
                }
                if (channel != null && !channel.isUser(userId)) {
                    channel.getUsers().put(userId, initiator);
                }
            }
        }

        UserJoinEvent event = new UserJoinEvent(client, network, channel, user, initiator);
        postEvent(event, messageId);
    }

    private void postEvent(Event event, String messageId) {
        bot.getEventManager().post(event, messageId);
    }
}
