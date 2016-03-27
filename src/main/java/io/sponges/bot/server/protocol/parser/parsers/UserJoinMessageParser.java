package io.sponges.bot.server.protocol.parser.parsers;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.channel.GroupChannel;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.api.event.events.user.UserJoinEvent;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.entities.NetworkImpl;
import io.sponges.bot.server.entities.UserImpl;
import io.sponges.bot.server.entities.channel.GroupChannelImpl;
import io.sponges.bot.server.protocol.parser.framework.MessageParser;
import org.json.JSONObject;

public final class UserJoinMessageParser extends MessageParser {

    private final Bot bot;

    public UserJoinMessageParser(Bot bot) {
        super("USER_JOIN");
        this.bot = bot;
    }

    @Override
    public void parse(Client client, long time, JSONObject content) {
        NetworkImpl network;
        {
            String id = content.getString("network");
            NetworkManager manager = client.getNetworkManager();
            if (manager.isNetwork(id)) {
                network = (NetworkImpl) manager.getNetwork(id);
            } else {
                network = new NetworkImpl(id, client);
                manager.getNetworks().put(id, network);
            }
        }

        GroupChannel channel = null;
        if (!content.isNull("channel")) {
            JSONObject json = content.getJSONObject("channel");
            String id = json.getString("id");
            ChannelManager manager = network.getChannelManager();
            if (manager.isChannel(id)) {
                channel = (GroupChannel) manager.getChannel(id);
            } else {
                channel = new GroupChannelImpl(id, network);
                manager.getChannels().put(id, channel);
            }
        }

        UserImpl user;
        UserImpl initiator = null;
        {
            {
                JSONObject json = content.getJSONObject("added");
                String userId = json.getString("id");
                boolean isAdmin = json.getBoolean("admin");
                boolean isOp = json.getBoolean("op");
                if (network.isUser(userId)) {
                    user = (UserImpl) network.getUser(userId);
                } else {
                    user = new UserImpl(userId, network, isAdmin, isOp);
                    if (!json.isNull("username")) user.setUsername(json.getString("username"));
                    if (!json.isNull("display-name")) user.setDisplayName(json.getString("display-name"));
                    network.addUser(user);
                }
                if (channel != null && !channel.isUser(userId)) {
                    channel.getUsers().put(userId, user);
                }
            }
            if (!content.isNull("initiator")) {
                JSONObject json = content.getJSONObject("initiator");
                String userId = json.getString("id");
                boolean isAdmin = json.getBoolean("admin");
                boolean isOp = json.getBoolean("op");
                if (network.isUser(userId)) {
                    initiator = (UserImpl) network.getUser(userId);
                } else {
                    initiator = new UserImpl(userId, network, isAdmin, isOp);
                    if (!json.isNull("username")) initiator.setUsername(json.getString("username"));
                    if (!json.isNull("display-name")) initiator.setDisplayName(json.getString("display-name"));
                    network.addUser(initiator);
                }
                if (channel != null && !channel.isUser(userId)) {
                    channel.getUsers().put(userId, initiator);
                }
            }
        }

        UserJoinEvent event = new UserJoinEvent(client, network, channel, user, initiator);

        Storage storage = bot.getStorage();
        boolean networkLoaded = storage.isLoaded(network);
        boolean channelLoaded = channel == null || storage.isLoaded(channel);
        if (!networkLoaded && !channelLoaded) {
            final GroupChannel finalChannel = channel;
            storage.load(network, networkData -> {
                storage.load(finalChannel, channelData -> {
                    bot.getEventManager().post(event);
                });
            });
        } else if (!networkLoaded) {
            storage.load(network, networkData -> {
                bot.getEventManager().post(event);
            });
        } else if (!channelLoaded) {
            bot.getStorage().load(channel, channelData -> {
                bot.getEventManager().post(event);
            });
        } else {
            bot.getEventManager().post(event);
        }
    }
}
