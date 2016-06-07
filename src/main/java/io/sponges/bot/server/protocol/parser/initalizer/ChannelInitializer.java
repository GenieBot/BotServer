package io.sponges.bot.server.protocol.parser.initalizer;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.entities.channel.GroupChannelImpl;
import io.sponges.bot.server.entities.channel.PrivateChannelImpl;

public final class ChannelInitializer {

    public static Channel createChannel(Storage storage, Network network, String id, boolean isPrivate) {
        if (isPrivate) {
            return new PrivateChannelImpl(id, network, storage);
        } else {
            return new GroupChannelImpl(id, network, storage);
        }
    }

}
