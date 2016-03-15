package io.sponges.bot.server.protocol.parser;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Message;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.channel.GroupChannel;
import io.sponges.bot.api.entities.channel.PrivateChannel;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.api.event.events.user.UserChatEvent;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.entities.MessageImpl;
import io.sponges.bot.server.entities.NetworkImpl;
import io.sponges.bot.server.entities.UserImpl;
import io.sponges.bot.server.entities.channel.GroupChannelImpl;
import io.sponges.bot.server.entities.channel.PrivateChannelImpl;
import org.json.JSONObject;

import java.util.Date;

public final class ChatMessageParser extends MessageParser {

    private final Bot bot;

    public ChatMessageParser(Bot bot) {
        super("CHAT");
        this.bot = bot;
    }

    @Override
    public void parse(Client client, long time, JSONObject content) {
        Network network;
        {
            String id = content.getString("network");
            NetworkManager manager = client.getNetworkManager();
            if (manager.isNetwork(id)) {
                network = manager.getNetwork(id);
            } else {
                network = new NetworkImpl(id, client); // TODO instantiate network data
                manager.getNetworks().put(id, network);
            }
        }

        Channel channel;
        User user = null;
        {
            JSONObject json = content.getJSONObject("channel");
            String id = json.getString("id");
            ChannelManager manager = network.getChannelManager();
            JSONObject userJson = content.getJSONObject("user");
            String userId = userJson.getString("id");
            if (manager.isChannel(id)) {
                channel = manager.getChannel(id);
                if (channel instanceof PrivateChannel) {
                    PrivateChannel privateChannel = (PrivateChannel) channel;
                    if (privateChannel.getUser().getId().equals(userId)) {
                        user = privateChannel.getUser();
                    }
                } else {
                    GroupChannel groupChannel = (GroupChannel) channel;
                    if (groupChannel.isUser(userId)) {
                        user = groupChannel.getUser(userId);
                    } else {
                        user = new UserImpl(userId, network);
                        groupChannel.getUsers().put(userId, user);
                    }
                }
            } else {
                user = new UserImpl(userId, network);
                boolean isPrivate = json.getBoolean("private");
                if (isPrivate) {
                    channel = new PrivateChannelImpl(id, network, user); // TODO instantiate channel data
                } else {
                    channel = new GroupChannelImpl(id, network); // TODO instantiate channel data
                    ((GroupChannel) channel).getUsers().put(userId, user);
                }
                manager.getChannels().put(id, channel);
            }
        }

        Message message;
        {
            JSONObject json = content.getJSONObject("message");
            String msgContent = json.getString("content");
            long msgTime = json.getLong("time");
            Date date = new Date(msgTime);
            message = new MessageImpl(client, network, channel, user, date, msgContent);
        }

        UserChatEvent event = new UserChatEvent(client, network, channel, user, message);

        Storage storage = bot.getStorage();
        boolean networkLoaded = storage.isLoaded(network);
        boolean channelLoaded = storage.isLoaded(channel);

        if (!networkLoaded && !channelLoaded) {
            storage.load(network, networkData -> {
                storage.load(channel, channelData -> {
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
