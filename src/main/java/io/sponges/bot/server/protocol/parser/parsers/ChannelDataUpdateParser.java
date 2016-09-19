package io.sponges.bot.server.protocol.parser.parsers;

import io.sponges.bot.api.Logger;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.api.entities.manager.UserManager;
import io.sponges.bot.api.event.events.channel.ChannelDataUpdateEvent;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.entities.data.ChannelDataImpl;
import io.sponges.bot.server.event.framework.EventBus;
import io.sponges.bot.server.protocol.parser.framework.MessageParser;
import org.json.JSONObject;

public final class ChannelDataUpdateParser extends MessageParser {

    private final EventBus eventBus;

    public ChannelDataUpdateParser(EventBus eventBus) {
        super("CHANNEL_DATA_UPDATE");
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
        manager.loadUser(id, user -> handleUpdate(client, network, channel, user, content));
    }

    private void handleUpdate(Client client, Network network, Channel channel, User user, JSONObject content) {
        ChannelDataUpdateEvent.Detail detail = ChannelDataUpdateEvent.Detail.valueOf(content.getString("detail"));
        String oldValue;
        if (!content.isNull("old") && content.get("old") != null) {
            oldValue = content.getString("old");
        } else {
            oldValue = null;
        }
        String value = content.getString("value");
        ChannelDataImpl data = (ChannelDataImpl) channel.getChannelData();
        switch (detail) {
            case NAME:
                data.setName(value);
                break;
            case TOPIC:
                data.setTopic(value);
                break;
        }
        ChannelDataUpdateEvent event = new ChannelDataUpdateEvent(client, network, channel, user, detail, oldValue, value);
        eventBus.postAsync(event, cancelled -> {
            if (!cancelled) return;
            if (oldValue == null) {
                Bot.LOGGER.log(Logger.Type.DEBUG, "cant cancel channel data update event as oldValue is null");
                return;
            }
            switch (detail) {
                case NAME:
                    data.updateName(oldValue); // doesnt work properly on skype, the old value often gets confused
                    break;
                case TOPIC:
                    data.updateTopic(oldValue);
                    break;
            }
            Bot.LOGGER.log(Logger.Type.DEBUG, "updating old data again cus cancelled as fuck");
        });
    }
}
