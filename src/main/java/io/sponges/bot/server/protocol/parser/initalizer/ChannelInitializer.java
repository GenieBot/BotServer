package io.sponges.bot.server.protocol.parser.initalizer;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.server.entities.channel.GroupChannelImpl;
import io.sponges.bot.server.entities.channel.PrivateChannelImpl;
import org.json.JSONObject;

public final class ChannelInitializer {

    public static Channel createChannel(Network network, JSONObject json) {
        String id = json.getString("id");
        boolean isPrivate = !json.isNull("private") && json.getBoolean("private");
        if (isPrivate) {
            return new PrivateChannelImpl(id, network);
        } else {
            return new GroupChannelImpl(id, network);
        }
    }

}
