package io.sponges.bot.server.protocol.parser.initalizer;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.statement.insert.InsertChannelStatement;
import io.sponges.bot.server.database.statement.select.SelectChannelIdStatement;
import io.sponges.bot.server.entities.channel.GroupChannelImpl;
import io.sponges.bot.server.entities.channel.PrivateChannelImpl;

import java.util.UUID;

public final class ChannelInitializer extends Initializer {

    public static Channel createChannel(Database database, Network network, String sourceId, boolean isPrivate)
            throws Exception {
        UUID id = new SelectChannelIdStatement(database, network.getId(), sourceId).executeAsync().get();
        if (id == null) {
            id = generateNewUUID();
            new InsertChannelStatement(database, id, sourceId, network.getId()).executeAsync();
        }
        if (isPrivate) {
            return new PrivateChannelImpl(id, sourceId, network);
        } else {
            return new GroupChannelImpl(id, sourceId, network);
        }
    }

}
