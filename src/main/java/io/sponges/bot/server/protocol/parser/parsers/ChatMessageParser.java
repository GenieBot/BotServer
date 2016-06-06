package io.sponges.bot.server.protocol.parser.parsers;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Message;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.channel.GroupChannel;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.api.event.events.user.UserChatEvent;
import io.sponges.bot.api.event.framework.Event;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.entities.MessageImpl;
import io.sponges.bot.server.entities.NetworkImpl;
import io.sponges.bot.server.entities.manager.UserManagerImpl;
import io.sponges.bot.server.protocol.parser.framework.MessageParser;
import io.sponges.bot.server.protocol.parser.initalizer.ChannelInitializer;
import io.sponges.bot.server.protocol.parser.initalizer.NetworkInitializer;
import io.sponges.bot.server.protocol.parser.initalizer.UserInitializer;
import org.json.JSONObject;

import java.util.Date;

public final class ChatMessageParser extends MessageParser {

    private final Bot bot;
    private final Storage storage;

    public ChatMessageParser(Bot bot) {
        super("CHAT");
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
                if (userManager.isUser(userId)) {
                    user = userManager.getUser(userId);
                } else {
                    user = UserInitializer.createUser(storage, network, userJson);
                    userManager.addUser(user);
                }
                if (channel instanceof GroupChannel) {
                    GroupChannel groupChannel = (GroupChannel) channel;
                    if (!groupChannel.isUser(userId)) {
                        groupChannel.getUsers().put(userId, user);
                    }
                }
            } else {
                if (userManager.isUser(userId)) {
                    user = userManager.getUser(userId);
                } else {
                    user = UserInitializer.createUser(storage, network, userJson);
                    userManager.addUser(user);
                }
                channel = ChannelInitializer.createChannel(storage, network, json);
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
        postEvent(event, messageId);
    }

    private void postEvent(Event event, String messageId) {
        bot.getEventManager().post(event, messageId);
    }
}
