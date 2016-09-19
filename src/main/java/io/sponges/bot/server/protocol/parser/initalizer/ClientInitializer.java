package io.sponges.bot.server.protocol.parser.initalizer;

import io.netty.channel.Channel;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.statement.insert.InsertClientStatement;
import io.sponges.bot.server.database.statement.select.SelectClientIdStatement;
import io.sponges.bot.server.entities.ClientImpl;

import java.util.UUID;

public final class ClientInitializer extends Initializer {

    public static Client createClient(Database database, String sourceId, String defaultPrefix, Channel channel) throws Exception {
        UUID id = new SelectClientIdStatement(database, sourceId).executeAsync().get();
        if (id == null) {
            id = generateNewUUID();
            new InsertClientStatement(database, id, sourceId).executeAsync();
        }
        return new ClientImpl(database, id, sourceId, defaultPrefix, channel);
    }

}
