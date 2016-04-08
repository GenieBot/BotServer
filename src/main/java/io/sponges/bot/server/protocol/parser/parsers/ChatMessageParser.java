package io.sponges.bot.server.protocol.parser.parsers;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Message;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.channel.GroupChannel;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.api.event.events.user.UserChatEvent;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.entities.MessageImpl;
import io.sponges.bot.server.entities.NetworkImpl;
import io.sponges.bot.server.protocol.parser.framework.MessageParser;
import io.sponges.bot.server.protocol.parser.initalizer.ChannelInitializer;
import io.sponges.bot.server.protocol.parser.initalizer.NetworkInitializer;
import io.sponges.bot.server.protocol.parser.initalizer.UserInitializer;
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
        NetworkImpl network;
        {
            String id = content.getString("network");
            NetworkManager manager = client.getNetworkManager();
            if (manager.isNetwork(id)) {
                network = (NetworkImpl) manager.getNetwork(id);
            } else {
                network = (NetworkImpl) NetworkInitializer.createNetwork(client, id);
                manager.getNetworks().put(id, network);
            }
        }

        Channel channel;
        User user;
        {
            JSONObject json = content.getJSONObject("channel");
            String id = json.getString("id");
            ChannelManager manager = network.getChannelManager();
            JSONObject userJson = content.getJSONObject("user");
            String userId = userJson.getString("id");
            if (manager.isChannel(id)) {
                channel = manager.getChannel(id);
                if (network.isUser(userId)) {
                    user = network.getUser(userId);
                } else {
                    user = UserInitializer.createUser(network, userJson);
                    network.addUser(user);
                }
                if (channel instanceof GroupChannel) {
                    GroupChannel groupChannel = (GroupChannel) channel;
                    if (!groupChannel.isUser(userId)) {
                        groupChannel.getUsers().put(userId, user);
                    }
                }
            } else {
                if (network.isUser(userId)) {
                    user = network.getUser(userId);
                } else {
                    user = UserInitializer.createUser(network, userJson);
                    network.addUser(user);
                }
                channel = ChannelInitializer.createChannel(network, json);
                if (channel instanceof GroupChannel) {
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
